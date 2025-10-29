package eu.lpinto.universe.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import eu.lpinto.universe.api.dto.AbstractDTO;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Vítor Martins <vitor.martins@petuniversal.com>
 * @param <ENT> AbstractEntity
 * @param <DTO> AbstractEntity DTO
 * @param <DTS> AbstractJasminDTS
 */
public class RestClient<DTO extends AbstractDTO> {

    private static final JacksonJsonProvider JSON_PROVIDER;
    private static final ObjectMapper mapper = new ObjectMapper();

    private final String BEARER;

    static {

        /*
         * Deserialization
         */
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        /*
         * Serialization
         */
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.enable(SerializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        /*
         * Other
         */
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);

        mapper.registerModule(new JavaTimeModule());

        JSON_PROVIDER = new JacksonJsonProvider(mapper);
    }

    private final ClientBuilder clientBuilder;
    private final GenericType<Collection<DTO>> dtoList;
    private final Class<DTO> dtoClass;
    private final String endpoint; // if ending with / it is removed

    /*
     * Certs
     */
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};

    /*
     * Constructors
     */
    /**
     *
     * @param dtoClass Service's Data Transportation Object class
     * @param dtoList  Service's Data Transportation Object List class
     * @param endpoint Service endpoint, if ending with '/' the slash will be removed
     * @param bearer   An oAuth bearer token
     */
    public RestClient(final Class<DTO> dtoClass, final GenericType<Collection<DTO>> dtoList, final String endpoint, final String bearer) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            this.clientBuilder = ClientBuilder.newBuilder().sslContext(sc);

            this.dtoList = dtoList;
            this.dtoClass = dtoClass;
            if(endpoint.endsWith("/")) {
                this.endpoint = endpoint.substring(0, endpoint.length() - 1);
            } else {
                this.endpoint = endpoint;
            }
            this.BEARER = bearer;
        } catch(KeyManagementException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /*
     * Find All
     */
    /**
     * Queries the server's API for all the objects for this endpoint.
     *
     * @return A list containing the founded objects.
     */
    public Collection<DTO> findAll() {
        return findAll(null);
    }

    /**
     * Queries the server's API for all the objects for this endpoint.
     *
     * @param subPath a sub path to be appended to the endpoint. If provided must start with '/'
     *
     * @return A list containing the founded objects.
     */
    public Collection<DTO> findAll(final String subPath) {
        WebTarget target;
        if(subPath == null || subPath.isEmpty() || "/".equals(subPath)) {
            target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint);
        } else {
            target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint
                                                                          + (subPath.endsWith("/") ? subPath.substring(0, subPath.length() - 1) : subPath));
        }

        Response response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + BEARER).get();
        int status = response.getStatus();

        if(status == Response.Status.OK.getStatusCode()) {
            return response.readEntity(dtoList);

        } else {
            handleError(response, target);
            throw new Error("This code line should not be reachable");
        }
    }

    /*
     * Find
     */
    public Collection<DTO> find(final Object... params) {
        Map<String, Object> options = new HashMap<>(1 + params.length / 2);

        for(int i = 1; i < params.length; i += 2) {
            options.put("" + params[i - 1], "" + params[i]);
        }

        return find(options);
    }

    /**
     * Queries the server's API for a subset of this endpoint's objects using the provided parameters.
     *
     * @param params
     *
     * @return
     */
    public Collection<DTO> find(final Map<String, Object> params) {
        return find(null, params);
    }

    protected Collection<DTO> find(final String subPath, final Map<String, Object> params) {
        /* Url */
        StringBuilder sb = new StringBuilder(30);
        sb.append("?");

        for(Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String path = (null == subPath || subPath.isEmpty()) ? endpoint : endpoint + subPath;
        String queryParams = sb.toString();

        String url = (queryParams == null || queryParams.isEmpty()) ? path : path + queryParams.substring(0, queryParams.length() - 1);

        /* request */
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(url);

        Response response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + BEARER).get();

        if(response.getStatus() == Response.Status.OK.getStatusCode()) {
            Collection<DTO> x = response.readEntity(dtoList);
            return x;
        } else {
            handleError(response, target);
            throw new Error("This code line should not be reachable");
        }
    }

    /*
     * Retrieve
     */
    /**
     * Retrieves a resource from this endpoint for the specified id.
     *
     * @param id The id to find.
     *
     * @return The resource object if it exists or NULL otherwise.
     */
    public DTO retrieve(final Long id) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint + "/" + id);
        Response response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + BEARER).get();
        if(response.getStatus() == 200) {
            return response.readEntity(dtoClass);
        } else {
            throw new RuntimeException(response.readEntity(String.class));
        }
    }

    public File retrievePDF(final Long id) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint + "/" + id + "/pdf");
        Response response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + BEARER).get();
        return response.readEntity(File.class);
    }

    public DTO retrieveFromJasmin(final String aux, final Long companyID) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint + "/jasmin/" + aux + "?company=" + companyID);
        Response response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + BEARER).get();
        return response.readEntity(dtoClass);
    }

    public DTO retrieveSync(final Long id) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint + "/" + id + "/sync");
        Response response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + BEARER).get();
        return response.readEntity(dtoClass);
    }

    public DTO retrieve(final String subPath, final Map<String, Object> params) {
        /* Url */
        StringBuilder sb = new StringBuilder(30);
        sb.append("?");

        for(Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String path = (null == subPath || subPath.isEmpty()) ? endpoint : endpoint + "/" + subPath;
        String queryParams = sb.toString();

        String url = (queryParams == null || queryParams.isEmpty()) ? path : path + queryParams.substring(0, queryParams.length() - 1);

        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(url);
        Response response = target.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + BEARER).get();
        return response.readEntity(dtoClass);
    }

    /*
     * Create
     */
    public DTO create(DTO dto) {
        return create(null, dto);
    }

    protected DTO create(final String subPath, DTO dto) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(subPath == null ? endpoint : endpoint + subPath);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + BEARER)
                .header("Accept-Language", "pt")
                .post(Entity.entity(dto, MediaType.APPLICATION_JSON));
        int status = response.getStatus();

        if(status == Response.Status.CREATED.getStatusCode() || status == Response.Status.OK.getStatusCode()) {
            return response.readEntity(dtoClass);
        } else {
            handleError(response, target, dto);
            return null;
        }
    }

    public Collection<DTO> create(final String subPath, Collection<DTO> list) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(subPath == null ? endpoint : endpoint + subPath);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + BEARER)
                .post(Entity.entity(list, MediaType.APPLICATION_JSON));
        int status = response.getStatus();

        if(status == Response.Status.CREATED.getStatusCode() || status == Response.Status.OK.getStatusCode()) {
            return response.readEntity(dtoList);
        } else {
            System.out.println("PMS responded with error status: " + status);
            handleError(response, target, list);
            throw new AssertionError("This code line should not be reachable");
        }
    }

    /*
     * Update
     */
    public void update(DTO dto) {
        update(dto, "" + dto.getId());
    }

    public void update(DTO dto, String subPath) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint + "/" + subPath);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + BEARER)
                .put(Entity.entity(dto, MediaType.APPLICATION_JSON + "; charset=utf-8"));

        int status = response.getStatus();
        if(status != Response.Status.NO_CONTENT.getStatusCode() && status != Response.Status.OK.getStatusCode()) {
            handleError(response, target, dto);
        }
    }

    public Collection<DTO> update(final String subPath, Collection<DTO> list) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(subPath == null ? endpoint : endpoint + subPath);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + BEARER)
                .put(Entity.entity(list, MediaType.APPLICATION_JSON));
        int status = response.getStatus();

        if(status == Response.Status.CREATED.getStatusCode() || status == Response.Status.OK.getStatusCode()) {
            return response.readEntity(dtoList);
        } else {
            handleError(response, target, list);
            throw new AssertionError("This code line should not be reachable");
        }
    }

    /*
     * Delete
     */
    public void delete(DTO dto) {
        delete(dto, "" + dto.getId());
    }

    public void delete(DTO dto, String subPath) {
        WebTarget target = clientBuilder.register(JSON_PROVIDER).build().target(endpoint + "/" + subPath);

        Response response = target
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + BEARER)
                .delete();

        int status = response.getStatus();
        if(status != Response.Status.NO_CONTENT.getStatusCode() && status != Response.Status.OK.getStatusCode()) {
            handleError(response, target, dto);
        }
    }

    private void handleError(final Response response, final WebTarget target) {
        handleError(response, target, null);
    }

    private void handleError(final Response response, final WebTarget target, final Object dto) {
        int status = response.getStatus();

        if(status == Response.Status.NOT_FOUND.getStatusCode()) {
            throw new BadRequestException(response);

        } else if(status == Response.Status.UNAUTHORIZED.getStatusCode()) {
            throw new BadRequestException(response);

        } else if(status == Response.Status.BAD_REQUEST.getStatusCode()) {
            logResponseError(response, target, dto);
            throw new BadRequestException(response);

        } else {
            logResponseError(response, target, dto);
            throw new ServerErrorException(response);
        }
    }

    /*
     * Helpers
     */
    private void logResponseError(Response response, WebTarget target, Object dto) throws InternalError {
        System.out.println("\nRequest was:\n" + toJson(dto)
                           + (dto == null ? ""
                              : "\n----------------------------------------------------------------------------------------"
                                + "\nServer responded with code " + response.getStatus()
                                + "\nRequest to: " + target.getUri().toString()
                                + "\nResponse: \n" + response.readEntity(String.class))
        );
    }

    static private String toJson(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch(JsonProcessingException ex) {
            System.out.println("Cannot serialize object: " + obj);
            return "[cannot serialize]";
        }
    }
}
