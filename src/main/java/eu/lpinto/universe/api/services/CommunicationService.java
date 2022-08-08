package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Contact;
import eu.lpinto.universe.api.dto.Errors;
import static eu.lpinto.universe.api.services.AbstractService.unprocessableEntity;
import eu.lpinto.universe.api.util.StatusEmail;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Email;
import eu.lpinto.universe.persistence.entities.EmailConfig;
import eu.lpinto.universe.persistence.facades.EmailPlugin;
import eu.lpinto.universe.util.UniverseFundamentals;
import javax.ejb.Asynchronous;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST service for Contact.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("communications")
public class CommunicationService extends AbstractService {

    @POST
    @Asynchronous
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void create(@Suspended final AsyncResponse asyncResponse,
                       final @Context UriInfo uriInfo,
                       final @Context HttpHeaders headers,
                       final @HeaderParam(UniverseFundamentals.AUTH_USER_ID) Long userID,
                       final @HeaderParam("Accept-Language") String locale,
                       final Contact contact) {
        try {
            asyncResponse.resume(doCreate(userID, locale, contact));
        } catch (PreConditionException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, buildOptions(uriInfo, userID), contact);
            asyncResponse.resume(unprocessableEntity(new Errors(ex.getErrors())));
        }
    }

    public Response doCreate(final Long userID, final String locale, final Contact contact) throws PreConditionException {

        try {
            switch (contact.getType()) {
                case "email":
                    new EmailPlugin(new EmailConfig()).send(new Email(contact.getTo(), contact.getSubject(), contact.getMessage()));
                    break;
                default:
                    return badRequest("Contact type cannot  be empty");
            }

            return noContent();
        } catch (RuntimeException ex) {
            return internalError(ex);
        }
    }
}
