package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.User;
import eu.lpinto.universe.api.dts.UserDTS;
import eu.lpinto.universe.api.dto.Errors;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST CRUD service for User.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("users")
public class UserService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.User, User, eu.lpinto.universe.controllers.UserController, UserDTS> {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    @EJB
    private eu.lpinto.universe.controllers.UserController controller;

    public UserService() {
        super(UserDTS.T);
    }

    /*
     * Services
     */
    @Override
    public Response doFind(final UriInfo uriInfo, final Long userID) {
        try {
            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            Map<String, Object> options = new HashMap<>(10);

            /*
             * filter by hasClinic
             */
            List<String> hasClinics = queryParameters.get("hasClinic");

            /*
             * filter by me
             */
            List<String> mes = queryParameters.get("me");

            if (hasClinics != null) {
                if (hasClinics.size() == 1) {
                    if (hasClinics.get(0) == null || hasClinics.get(0).isEmpty()) {
                        return badRequest("Cannot search Users by hasClinics id with empty value");
                    } else {
                        Boolean hasClinic = Boolean.valueOf(hasClinics.get(0));
                        options.put("hasClinic", hasClinic);
                    }
                }
            }

            if (mes != null) {
                if (mes.size() == 1) {
                    if (mes.get(0) == null || mes.get(0).isEmpty()) {
                        return badRequest("Cannot search Users by me id with empty value");
                    } else {
                        return ok(UserDTS.T.toAPI(controller.retrieve(userID, userID)));
                    }
                }
            }

            return ok(UserDTS.T.toAPI(controller.find(userID, options)));

        } catch (UnknownIdException ex) {
            return unknown(userID);
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

    @POST
    @Path("passwordRecovery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recoverPassword(final String email) {
        if (email == null || email.isEmpty()) {
            return unprocessableEntity(new Errors().addError("token.email", "Missing email"));
        }

        try {
            controller.recoverPassword(email);
            return noContent();

        } catch (RuntimeException ex) {
            return internalError(ex);
        } catch (PreConditionException ex) {
            return unprocessableEntity(new Errors(ex.getErrors()));
        }
    }

    /*
     * Helpers
     */
    @Override
    protected eu.lpinto.universe.controllers.UserController getController() {
        return controller;
    }
}
