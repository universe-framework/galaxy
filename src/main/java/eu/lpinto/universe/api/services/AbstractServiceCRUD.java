package eu.lpinto.universe.api.services;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import eu.lpinto.universe.api.dto.Errors;
import eu.lpinto.universe.api.dto.UniverseDTO;
import eu.lpinto.universe.api.dts.AbstractDTS;
import eu.lpinto.universe.controllers.AbstractControllerCRUD;
import eu.lpinto.universe.controllers.CrudController;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
 * @param <E>   Domain AbstractEntityDTO
 * @param <D>   DTO
 * @param <C>   Controller
 * @param <DTS> DTS service
 */
public abstract class AbstractServiceCRUD<E extends UniverseEntity, D extends UniverseDTO, C extends AbstractControllerCRUD<E>, DTS extends AbstractDTS<E, D>> extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceCRUD.class);
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
                     final @HeaderParam(value = "userID") Long userID) throws PreConditionException {

        /* Setup */
        Map<String, Object> options = buildOptions(uriInfo, userID);

        /* Log request */
        logRequest(uriInfo, options, Thread.currentThread().getStackTrace()[1].getMethodName());

        /* Body */
        Response doFind = doFind(options);

        /* Log response */
        logResponse(options, uriInfo, doFind.getEntity(), Thread.currentThread().getStackTrace()[1].getMethodName());

        /* return */
        asyncResponse.resume(doFind);
    }

    public Response doFind(final Map<String, Object> options) throws PreConditionException {
        try {
            return ok(dts.toAPI(getController().find((Long) options.get("user"), options)));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return forbidden((Long) options.get("user"));

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);
        }
    }

    @POST
    @Asynchronous
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void create(@Suspended final AsyncResponse asyncResponse,
                       final @Context UriInfo uriInfo,
                       final @HeaderParam("userID") Long userID,
                       final @HeaderParam("Accept-Language") String locale,
                       final D dto) {
        /* Setup */
        Map<String, Object> options = buildOptions(uriInfo, userID);

        /* Log request */
        logRequest(uriInfo, options, Thread.currentThread().getStackTrace()[1].getMethodName(), dto);

        /* Body */
        Response asyncCreate = asyncCreate(userID, dto, options);

        /* Log response */
        logResponse(options, uriInfo, asyncCreate.getEntity(), Thread.currentThread().getStackTrace()[1].getMethodName());

        /* return */
        asyncResponse.resume(asyncCreate);
    }

    public Response asyncCreate(final Long userID, final D dto, final Map<String, Object> options) {
        try {
            return ok(doCreate(userID, dto, options));

        } catch (PreConditionException ex) {
            return unprocessableEntity(new Errors(ex.getErrors()));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return forbidden(userID);

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return badRequest("unknown id: [" + ex.getId() + "]");

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);
        }
    }

    public D doCreate(final Long userID, final D dto, final Map<String, Object> options) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        return dts.toAPI(getController().create(userID, options, dts.toDomain(dto)));
    }

    @GET
    @Asynchronous
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void retrieve(@Suspended final AsyncResponse asyncResponse,
                         final @Context UriInfo uriInfo,
                         final @HeaderParam("userID") Long userID,
                         final @PathParam("id") Long id) throws PermissionDeniedException {
        /* Setup */
        Map<String, Object> options = buildOptions(uriInfo, userID);

        /* Log request */
        logRequest(uriInfo, options, Thread.currentThread().getStackTrace()[1].getMethodName());

        /* Body */
        Response doRetrieve = doRetrieve(userID, id);

        /* Log response */
        logResponse(options, uriInfo, doRetrieve.getEntity(), Thread.currentThread().getStackTrace()[1].getMethodName());

        /* return */
        asyncResponse.resume(doRetrieve);
    }

    public Response doRetrieve(final Long userID, Long id) throws PermissionDeniedException {
        try {
            if (id == null) {
                return unprocessableEntity(new Errors().addError("entity.id", "Missing id"));
            }

            return ok(dts.toAPI(getController().retrieve(userID, new HashMap<>(0), id)));

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return unknown(id);

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return forbidden(userID);
        } catch (PreConditionException ex) {
            return unprocessableEntity(new Errors(ex.getErrors()));
        }
    }

    @PUT
    @Path("{id}")
    @Asynchronous
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void update(@Suspended final AsyncResponse asyncResponse,
                       final @Context UriInfo uriInfo,
                       final @HeaderParam("userID") Long userID,
                       final @PathParam("id") Long id,
                       final D dto) {
        /* Setup */
        Map<String, Object> options = buildOptions(uriInfo, userID);

        /* Log request */
        logRequest(uriInfo, options, Thread.currentThread().getStackTrace()[1].getMethodName(), dto);

        /* Body */
        Response doUpdate = doUpdate(userID, id, dto);

        /* Log response */
        logResponse(options, uriInfo, doUpdate.getEntity(), Thread.currentThread().getStackTrace()[1].getMethodName());

        /* return */
        asyncResponse.resume(doUpdate);
    }

    public Response doUpdate(final Long userID, final Long id, final D dto) {
        try {
            /*
             * Preconditions
             */
            if (dto.getId() == null) {
                dto.setId(id);

            } else if (!dto.getId().equals(id)) {
                return mismatchID(id, dto.getId());
            }

            /*
             * Body
             */
            getController().update(userID, new HashMap<>(0), dts.toDomain(dto));
            return noContent();

        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return unknown(id);

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return forbidden(userID);

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);
        } catch (PreConditionException ex) {
            return unprocessableEntity(new Errors(ex.getErrors()));
        }
    }

    @DELETE
    @Asynchronous
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@Suspended final AsyncResponse asyncResponse,
                       final @Context UriInfo uriInfo,
                       final @HeaderParam("userID") Long userID, @PathParam("id") final Long id) {
        /* Setup */
        Map<String, Object> options = buildOptions(uriInfo, userID);

        /* Log request */
        logRequest(uriInfo, options, Thread.currentThread().getStackTrace()[1].getMethodName());

        /* Body */
        Response doDelete = doDelete(userID, id);

        /* Log response */
        logResponse(options, uriInfo, doDelete.getEntity(), Thread.currentThread().getStackTrace()[1].getMethodName());

        /* return */
        asyncResponse.resume(doDelete);
    }

    public Response doDelete(final Long userID, final Long id) {
        try {
            getController().delete(userID, new HashMap<>(0), id);
            return noContent();
        } catch (UnknownIdException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return unknown(id);
        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return forbidden(userID);
        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);
        } catch (PreConditionException ex) {
            return unprocessableEntity(new Errors(ex.getErrors()));
        }
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

    private static String optionsStr(final Map<String, Object> options) {
        String result = "";
        for (Map.Entry<String, Object> a : options.entrySet()) {
            result += '\t' + a.getKey() + " : " + toJson(a.getValue()) + "\n";
        }

        return result;

    }

    private Map<String, Object> buildOptions(final UriInfo uriInfo, final Long userID) {
        Map<String, Object> options = new HashMap<>(uriInfo.getQueryParameters().size());

        options.put("request", UUID.randomUUID().toString());
        options.put("startMillis", System.currentTimeMillis());

        options.put("user", userID);

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        for (String key : queryParameters.keySet()) {
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
        }

        return options;
    }

    private void logRequest(final UriInfo uriInfo, Map<String, Object> options, final String methodName) {
        logRequest(uriInfo, options, methodName, null);
    }

    private void logRequest(final UriInfo uriInfo, Map<String, Object> options, final String methodName, final D dto) {
        LOGGER.debug("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ -> REQUEST \\\\\n\tID: {}\n\tServide: {}#{}\n{}\n////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////",
                     options.get("request"), uriInfo.getPath().substring(1), methodName, dto == null ? "" : toJson(dto)
        );
    }

    private void logResponse(final Map<String, Object> options, final UriInfo uriInfo, final Object response, final String methodName) {
        String body;

        if (response instanceof List && ((List) response).size() > 3) {
            body = "Result: " + ((List) response).size() + " objects.";
        } else {
            body = toJson(response);
        }

        LOGGER.debug("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ RESPONSE ->\n\tID: {}\n\tServide: {}#{}\n\t Duration: {}\n{}\n////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////",
                     options.get("request"), uriInfo.getPath().substring(1), methodName, (System.currentTimeMillis() - (Long) options.get("startMillis")), body
        );
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

    /*
     * Getters / Setters
     */
    protected abstract CrudController<E> getController();

    protected DTS getDts() {
        return dts;
    }
}
