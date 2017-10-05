package eu.lpinto.universe.api.services;

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
    public final void find(@Suspended final AsyncResponse asyncResponse,
                           final @Context UriInfo uriInfo,
                           final @HeaderParam(value = "userID") Long userID) throws PreConditionException {

        Map<String, Object> options = new HashMap<>(uriInfo.getQueryParameters().size());

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        for (String key : queryParameters.keySet()) {
            List<String> values = queryParameters.get(key);

            if (values != null && !values.isEmpty() && values.size() == 1) {

                try {
                    Long l = Long.valueOf(values.get(0));
                    options.put(key, l);
                } catch (NumberFormatException ex) {
                    options.put(key, values.get(0));
                }
            }
        }

        options.put("user", userID);

        asyncResponse.resume(doFind(options));
    }

    public Response doFind(final Map<String, Object> options) throws PreConditionException {
        try {
            return ok(dts.toAPI(getController().find((Long) options.get("user"), options)));

        } catch (PermissionDeniedException ex) {
            LOGGER.debug(ex.getMessage(), ex);
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
    public final void create(@Suspended final AsyncResponse asyncResponse,
                             final @HeaderParam("userID") Long userID,
                             final @HeaderParam("Accept-Language") String locale,
                             final D dto) {
        asyncResponse.resume(doCreate(userID, locale, dto));
    }

    public Response doCreate(final Long userID, final String locale, final D dto) {
        try {
            return ok(dts.toAPI(getController().create(userID, dts.toDomain(dto))));

        } catch (PreConditionException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return conflict(ex.getMessage());

        } catch (PermissionDeniedException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return forbidden(userID);

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);
        }
    }

    @GET
    @Asynchronous
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public final void retrieve(@Suspended final AsyncResponse asyncResponse,
                               final @Context UriInfo uriInfo,
                               final @HeaderParam("userID") Long userID,
                               final @PathParam("id") Long id) throws PermissionDeniedException {
        asyncResponse.resume(doRetrieve(userID, id));
    }

    public Response doRetrieve(final Long userID, Long id) throws PermissionDeniedException {
        try {
            if (id == null) {
                return unprocessableEntity(new Errors().addError("entity.id", "Missing id"));
            }

            return ok(dts.toAPI(getController().retrieve(userID, id)));

        } catch (UnknownIdException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return unknown(id);

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);

        } catch (PermissionDeniedException ex) {
            LOGGER.debug(ex.getMessage(), ex);
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
    public final void update(@Suspended final AsyncResponse asyncResponse,
                             final @Context UriInfo uriInfo,
                             final @HeaderParam("userID") Long userID,
                             final @PathParam("id") Long id,
                             final D dto) {
        asyncResponse.resume(doUpdate(userID, id, dto));
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
            getController().update(userID, dts.toDomain(dto));
            return noContent();

        } catch (UnknownIdException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return unknown(id);

        } catch (PermissionDeniedException ex) {
            LOGGER.debug(ex.getMessage(), ex);
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
    public final void delete(@Suspended final AsyncResponse asyncResponse,
                             final @HeaderParam("userID") Long userID, @PathParam("id") final Long id) {
        asyncResponse.resume(doDelete(userID, id));
    }

    public Response doDelete(final Long userID, final Long id) {
        try {
            getController().delete(userID, id);
            return noContent();
        } catch (UnknownIdException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return unknown(id);
        } catch (PermissionDeniedException ex) {
            LOGGER.debug(ex.getMessage(), ex);
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

    /*
     * Getters / Setters
     */
    protected abstract CrudController<E> getController();

    protected DTS getDts() {
        return dts;
    }
}
