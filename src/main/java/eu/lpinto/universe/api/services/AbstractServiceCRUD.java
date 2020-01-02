package eu.lpinto.universe.api.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import eu.lpinto.universe.api.dto.Errors;
import eu.lpinto.universe.api.dto.UniverseDTO;
import eu.lpinto.universe.api.dts.UniverseDTS;
import eu.lpinto.universe.api.util.StatusEmail;
import eu.lpinto.universe.controllers.AbstractControllerCRUD;
import eu.lpinto.universe.controllers.CrudController;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.ejb.Asynchronous;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST service interface for users.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <E> Domain AbstractEntityDTO
 * @param <D> DTO
 * @param <C> Controller
 * @param <DTS> DTS service
 */
public abstract class AbstractServiceCRUD<E extends UniverseEntity, D extends UniverseDTO, C extends AbstractControllerCRUD<E>, DTS extends UniverseDTS<E, D>>
        extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceCRUD.class);

    private static String optionsStr(final Map<String, Object> options) {
        String result = "";
        for (Map.Entry<String, Object> a : options.entrySet()) {
            result += "\t" + a.getKey() + " : " + toJson(a.getValue()) + "\n";
        }

        return result;
    }

    private static String toJson(final Object obj) {
        ObjectMapper mapper = new ObjectMapper();
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
        try {
            return mapper.writeValueAsString(obj).replace(System.lineSeparator(), System.lineSeparator() + '\t');
        } catch (JsonProcessingException ex) {
            LOGGER.error("Cannot serialize object: " + obj);
            return "[cannot serialize]";
        }
    }

    private final DTS dts;

    /*
     * Constructors
     */
    public AbstractServiceCRUD(final DTS dts) {
        this.dts = dts;
    }

    /*
     * Services
     */
    @GET
    @Asynchronous
    @Produces(value = MediaType.APPLICATION_JSON)
    public void find(@Suspended final AsyncResponse asyncResponse,
                     final @Context UriInfo uriInfo,
                     final @Context HttpHeaders headers,
                     final @HeaderParam(value = "userID") Long userID) throws PreConditionException {
        Map<String, Object> options = new HashMap<>(10 + uriInfo.getQueryParameters().size());

        try {

            /* Setup */
            buildOptions(options, uriInfo, userID);


            /* Log request */
            logRequest(uriInfo, options, currentMethod());

            /* Body */
            Response result = doFind(options);

            /* Log response */
            options.put("service.end", System.currentTimeMillis());
            logResponse(uriInfo, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(forbidden((Long) options.get("user")));

        } catch (RuntimeException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(internalError(ex));
        }
    }

    public Response doFind(final Map<String, Object> options) throws PreConditionException, PermissionDeniedException {
        return ok(dts.toAPI(getController().find((Long) options.get("user"), options)));
    }

    @POST
    @Path("list")
    @Asynchronous
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createList(@Suspended final AsyncResponse asyncResponse,
                           final @Context UriInfo uriInfo,
                           final @Context HttpHeaders headers,
                           final @HeaderParam("userID") Long userID,
                           final @HeaderParam("Accept-Language") String locale,
                           final List<D> dto) {
        Map<String, Object> options = new HashMap<>(10 + uriInfo.getQueryParameters().size());

        try {

            /* Setup */
            buildOptions(options, uriInfo, userID);
            options.put("locale", locale);

            /* Log request */
            logRequest(uriInfo, options, currentMethod(), dto.get(0).getClass().getSimpleName() + "([" + dto.size() + " ])");

            /* Body */
            Response result = doCreate(userID, dto, options);

            /* Log response */
            options.put("service.end", System.currentTimeMillis());
            logResponse(uriInfo, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PreConditionException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(unprocessableEntity(new Errors(ex.getErrors())));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(forbidden(userID));

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(badRequest("unknown id: [" + ex.getId() + "]"));

        } catch (RuntimeException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(internalError(ex));
        }
    }

    public Response doCreate(final Long userID, final List<D> dto, final Map<String, Object> options) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        return ok(dts.toAPI(getController().create(userID, options, dts.toDomain(dto))));
    }

    @POST
    @Asynchronous
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void create(@Suspended final AsyncResponse asyncResponse,
                       final @Context UriInfo uriInfo,
                       final @Context HttpHeaders headers,
                       final @HeaderParam("userID") Long userID,
                       final @HeaderParam("Accept-Language") String locale,
                       final D dto) {
        Map<String, Object> options = new HashMap<>(10 + uriInfo.getQueryParameters().size());
        options.put("locale", new Locale(locale));

        try {

            /* Setup */
            buildOptions(options, uriInfo, userID);
            options.put("locale", locale);

            /* Log request */
            logRequest(uriInfo, options, currentMethod(), dto);

            /* Body */
            Response result = doCreate(userID, dto, options);

            /* Log response */
            options.put("service.end", System.currentTimeMillis());
            logResponse(uriInfo, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PreConditionException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options, dto);
            asyncResponse.resume(unprocessableEntity(new Errors(ex.getErrors())));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options, dto);
            asyncResponse.resume(forbidden(userID));

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options, dto);
            asyncResponse.resume(badRequest("unknown id: [" + ex.getId() + "]"));

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options, dto);
            asyncResponse.resume(internalError(ex));
        }
    }

    public Response doCreate(final Long userID, final D dto, final Map<String, Object> options) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        return ok(dts.toAPI(getController().create(userID, options, dts.toDomain(dto))));
    }

    @GET
    @Asynchronous
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void retrieve(@Suspended
            final AsyncResponse asyncResponse,
                         final @Context UriInfo uriInfo,
                         final @Context HttpHeaders headers,
                         final @HeaderParam("userID") Long userID,
                         final @PathParam("id") Long id) throws PermissionDeniedException {
        Map<String, Object> options = new HashMap<>(10 + uriInfo.getQueryParameters().size());

        try {

            /* Setup */
            buildOptions(options, uriInfo, userID);

            /* Log request */
            logRequest(uriInfo, options, currentMethod());

            /* Body */
            Response result = doRetrieve(userID, id, options);

            /* Log response */
            options.put("service.end", System.currentTimeMillis());
            logResponse(uriInfo, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PreConditionException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(unprocessableEntity(new Errors(ex.getErrors())));

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(unknown(id));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(forbidden(userID));

        } catch (RuntimeException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(internalError(ex));
        }
    }

    public Response doRetrieve(final Long userID, final Long id, final Map<String, Object> options) throws PermissionDeniedException, UnknownIdException, PreConditionException {
        if (id == null) {
            return unprocessableEntity(new Errors().addError("entity.id", "Missing id"));
        }

        return ok(dts.toAPI(getController().retrieve(userID, options, id)));
    }

    @PUT
    @Path("{id}")
    @Asynchronous
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void update(@Suspended
            final AsyncResponse asyncResponse,
                       final @Context UriInfo uriInfo,
                       final @Context HttpHeaders headers,
                       final @HeaderParam("userID") Long userID,
                       final @PathParam("id") Long id,
                       final D dto) {
        Map<String, Object> options = new HashMap<>(10 + uriInfo.getQueryParameters().size());

        try {

            /* Setup */
            buildOptions(options, uriInfo, userID);

            /* Log request */
            logRequest(uriInfo, options, currentMethod(), dto);

            /* Preconditions */
            if (dto.getId() == null) {
                dto.setId(id);

            } else if (!dto.getId().equals(id)) {
                asyncResponse.resume(mismatchID(id, dto.getId()));
            }

            /* Body */
            Response result = doUpdate(userID, dto, options);

            /* Log response */
            options.put("service.end", System.currentTimeMillis());
            logResponse(uriInfo, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PreConditionException ex) {
            asyncResponse.resume(unprocessableEntity(new Errors(ex.getErrors())));

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            asyncResponse.resume(unknown(dto.getId()));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            asyncResponse.resume(forbidden(userID));

        } catch (RuntimeException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options, dto);
            asyncResponse.resume(internalError(ex));
        }
    }

    public Response doUpdate(final Long userID, final D dto, final Map<String, Object> options) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        getController().update(userID, options, dts.toDomain(dto));
        return noContent();
    }

    @DELETE
    @Asynchronous
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Suspended
            final AsyncResponse asyncResponse,
                       final @Context UriInfo uriInfo,
                       final @Context HttpHeaders headers,
                       final @HeaderParam("userID") Long userID, @PathParam("id")
                       final Long id) {
        Map<String, Object> options = new HashMap<>(10 + uriInfo.getQueryParameters().size());

        try {

            /* Setup */
            buildOptions(options, uriInfo, userID);

            /* Log request */
            logRequest(uriInfo, options, currentMethod());

            /* Body */
            Response result = doDelete(userID, id);

            /* Log response */
            options.put("service.end", System.currentTimeMillis());
            logResponse(uriInfo, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PreConditionException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(unprocessableEntity(new Errors(ex.getErrors())));

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(unknown(id));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(forbidden(userID));

        } catch (RuntimeException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(internalError(ex));
        }
    }

    public Response doDelete(final Long userID, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        getController().delete(userID, new HashMap<>(0), id);
        return noContent();
    }

    /*
     * Helpers
     */
    protected void addSKeyString(String key, MultivaluedMap<String, String> queryParameters, Map<String, Object> options) throws IllegalArgumentException {
        List<String> keys = queryParameters.get(key);

        try {
            if (keys != null && !keys.isEmpty() && keys.size() == 1) {
                options.put(key, Long.valueOf(keys.get(0)));
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid value: [" + keys.get(0) + "] for option: [" + key + "]");
        }
    }

    protected void addSKeyCalendar(String key, MultivaluedMap<String, String> queryParameters, Map<String, Object> options) throws IllegalArgumentException {
        List<String> keys = queryParameters.get(key);
        if (keys != null && !keys.isEmpty() && keys.size() == 1) {
            Calendar startedAfter = Calendar.getInstance();
            try {
                startedAfter.setTime(new ISO8601DateFormat().parse(keys.get(0)));
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Invalid value for openAfter: " + keys.get(0));
            }
            options.put(key, startedAfter);
        }
    }

    protected Map<String, Object> buildOptions(final Map<String, Object> options, final UriInfo uriInfo, final Long userID) {
        options.put("startMillis", System.currentTimeMillis());
        options.put("request", UUID.randomUUID().toString());

        options.put("user", userID);

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        queryParameters.keySet().forEach(key -> {
            List<String> values = queryParameters.get(key);
            if (values != null && !values.isEmpty() && values.size() == 1) {
                if ("true".equalsIgnoreCase(values.get(0))) {
                    options.put(key, Boolean.TRUE);

                } else if ("false".equalsIgnoreCase(values.get(0))) {
                    options.put(key, Boolean.FALSE);

                } else {
                    try {
                        Long l = Long.valueOf(values.get(0));
                        options.put(key, l);
                    } catch (NumberFormatException ex) {
                        options.put(key, values.get(0));
                    }
                }
            }
        });

        options.put("service.start", System.currentTimeMillis());

        return options;
    }

    protected void logRequest(final UriInfo uriInfo, Map<String, Object> options, final String methodName) {
        logRequest(uriInfo, options, methodName, null);
    }

    protected void logRequest(final UriInfo uriInfo, Map<String, Object> options, final String methodName, final Object dto) {
        String body;

        if (dto == null) {
            body = "'null'";

        } else if (dto instanceof Collection && ((Collection) dto).size() > 3) {
            body = "Result: " + ((List) dto).size() + " objects.";

        } else {
            body = toJson(dto);
        }

        LOGGER.debug("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ -> REQUEST \\\\"
                     + "\n\tID: {}"
                     + "\n\t{}" // URL
                     + "\n\t{}({})" // java function and service url
                     + "\n{}"
                     + "\n\t{}"
                     + "\n////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////",
                     options.get("request"),
                     uriInfo.getRequestUri(),
                     uriInfo.getPath().substring(1), methodName,
                     optionsStr(options), body
        );
    }

    protected void logResponse(final UriInfo uriInfo, final Map<String, Object> options, final String methodName, final Object response) {
        String body;

        if (response == null) {
            body = "'null'";

        } else if (response instanceof Collection && ((Collection) response).size() > 3) {
            body = "Result: " + ((List) response).size() + " objects.";
        } else {
            body = toJson(response);
        }

        Long serviceDuration = null;
        Long controllerDuration = null;
        if (options != null) {
            if (options.containsKey("service.start") && options.containsKey("service.end")) {
                serviceDuration = ((Long) options.get("service.end")) - (Long) (options.get("service.start"));
            }
            if (options.containsKey("controller.start") && options.containsKey("controller.end")) {
                controllerDuration = ((Long) options.get("controller.end")) - (Long) (options.get("controller.start"));
            }
        }

        LOGGER.debug("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ RESPONSE ->"
                     + "\n\tID: {}"
                     + "\n\t{}" // URL
                     + "\n\t{}({})" // java function and service url
                     + "\n\tDuration: {}"
                     + "\n\tService: {}"
                     + "\n\tController: {}"
                     + "\n{}"
                     + "\n\t{}"
                     + "\n////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////",
                     options.get("request"),
                     uriInfo.getRequestUri(),
                     uriInfo.getPath().substring(1), methodName,
                     (System.currentTimeMillis() - (Long) options.get("startMillis")),
                     serviceDuration,
                     controllerDuration,
                     optionsStr(options),
                     body);
    }

    private String currentMethod() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    /*
     * Getters / Setters
     */
    protected abstract CrudController<E> getController();

    protected DTS getDts() {
        return dts;
    }
}
