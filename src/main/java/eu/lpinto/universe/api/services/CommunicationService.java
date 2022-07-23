package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Contact;
import eu.lpinto.universe.persistence.facades.EmailFacade;
import eu.lpinto.universe.util.UniverseFundamentals;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST service for Contact.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("communications")
public class CommunicationService extends AbstractService {

    @EJB
    private EmailFacade facade;

    @POST
    @Asynchronous
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void create(@Suspended final AsyncResponse asyncResponse,
                       final @HeaderParam(UniverseFundamentals.AUTH_USER_ID) Long userID,
                       final @HeaderParam("Accept-Language") String locale,
                       final Contact contact) {
        asyncResponse.resume(doCreate(userID, locale, contact));
    }

    public Response doCreate(final Long userID, final String locale, final Contact contact) {

        try {
            switch (contact.getType()) {
                case "email":
                    facade.sendEmail(contact.getTo(), contact.getSubject(), contact.getMessage());
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
