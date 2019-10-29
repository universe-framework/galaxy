package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Errors;
import eu.lpinto.universe.api.dto.User;
import eu.lpinto.universe.api.dts.UserDTS;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    @POST
    @Path("passwordRecovery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recoverPassword(final @HeaderParam("Accept-Language") String locale,
                                    final String email) {
        if (email == null || email.isEmpty()) {
            return unprocessableEntity(new Errors().addError("token.email", "Missing email"));
        }

        try {
            controller.recoverPassword(locale, email);
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
