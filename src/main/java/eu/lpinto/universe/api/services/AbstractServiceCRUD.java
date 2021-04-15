package eu.lpinto.universe.api.services;

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
 * @param <E> Domain AbstractEntityDTO
 * @param <D> DTO
 * @param <C> Controller
 * @param <DTS> DTS service
 */
public abstract class AbstractServiceCRUD<E extends UniverseEntity, D extends UniverseDTO, C extends AbstractControllerCRUD<E>, DTS extends UniverseDTS<E, D>>
        extends AbstractService {

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
            logResponse(uriInfo, headers, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(forbidden((Long) options.get("user")));

        } catch (RuntimeException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(noContent());
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
            logRequest(uriInfo, options, currentMethod(), dto.get(0).getClass().getSimpleName() + "[" + dto.size() + "]");

            /* Body */
            Response result = doCreate(userID, dto, options);

            /* Log response */
            options.put("service.end", System.currentTimeMillis());
            logResponse(uriInfo, headers, options, currentMethod(), result.getEntity());

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
            asyncResponse.resume(unknown(ex.getId()));

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
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
            logResponse(uriInfo, headers, options, currentMethod(), result.getEntity());

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
            asyncResponse.resume(unknown(ex.getId()));

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
            logResponse(uriInfo, headers, options, currentMethod(), result.getEntity());

            /* return */
            asyncResponse.resume(result);

        } catch (PreConditionException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(unprocessableEntity(new Errors(ex.getErrors())));

        } catch (UnknownIdException ex) {
            asyncResponse.resume(unknown(id));

        } catch (PermissionDeniedException ex) {
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(forbidden(userID));

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
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
            logResponse(uriInfo, headers, options, currentMethod(), result.getEntity());

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
            LOGGER.error(ex.getMessage(), ex);
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
            logResponse(uriInfo, headers, options, currentMethod(), result.getEntity());

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
            LOGGER.error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(internalError(ex));
        }
    }

    public Response doDelete(final Long userID, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        getController().delete(userID, new HashMap<>(0), id);
        return noContent();
    }

    /*
     * Getters / Setters
     */
    protected abstract CrudController<E> getController();

    protected DTS getDts() {
        return dts;
    }
}
