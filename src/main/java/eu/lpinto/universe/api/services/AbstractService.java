package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Errors;
import eu.lpinto.universe.api.dto.FaultDTO;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic functionalities for all REST services.
 *
 * @author Luís Pinto <code>- mail@lpinto.eu</code>
 */
public abstract class AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    /*
     * Logger
     */
    protected static Logger getLogger() {
        return LOGGER;
    }

    protected static void debug(final Exception ex) {
        getLogger().debug(firstCause(ex), ex);
    }

    /*
     * Helpers
     */
    protected static String firstCause(final Throwable ex) {
        if (ex.getCause() == null) {

            String message = ex.getMessage();

            if (message != null && message.contains(": ")) {
                message = message.split(": ")[1];
            }

            return message;

        }
        else {
            return firstCause(ex.getCause());
        }
    }

    /*
     * Responses
     */
    protected static Response response(final Response.Status status, Object msg) {
        return Response.status(status).entity(msg).build();
    }

    protected static Response ok(final Object object) {
        return Response.ok(object).build();
    }

    protected static Response created(final UriBuilder url) {
        return Response.status(Response.Status.CREATED).header("Location", url).build();
    }

    protected static Response noContent() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    protected static Response forbidden(final Long userID) {
        return Response.status(Response.Status.FORBIDDEN).entity(new FaultDTO("Missing owner permissions for person: " + userID)).build();
    }

    protected static Response mismatchID(final Long pathID, final Long objID) {
        return unprocessableEntity(new Errors().addError("id", "Path id[" + pathID + "] differs from entity's id[" + objID + "]"));
    }

    protected static Response badRequest(final String msg) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new FaultDTO(msg)).build();
    }

    protected static Response unprocessableEntity(final String field, final String errorMsg) {
        return unprocessableEntity(new Errors().addError(field, errorMsg));
    }

    protected static Response unprocessableEntity(final Errors error) {
        return Response.status(422).entity(error).build();
    }

    protected static Response unknown(final Long id) {
        return Response.status(Response.Status.NOT_FOUND).entity(new FaultDTO("Unknown entity [id=" + id + "]")).build();

    }

    protected static Response conflict(final String msg) {
        return Response.status(Response.Status.CONFLICT).entity(new FaultDTO(msg)).build();
    }

    protected static Response internalError(final Exception ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new FaultDTO(firstCause(ex))).build();
    }

    /*
     * Constructtors
     */
    protected AbstractService() {
        super();
    }
}
