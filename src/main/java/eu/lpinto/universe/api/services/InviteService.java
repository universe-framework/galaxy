package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dts.InviteDTS;
import static eu.lpinto.universe.api.services.AbstractService.getLogger;
import eu.lpinto.universe.api.util.StatusEmail;
import eu.lpinto.universe.controllers.InviteController;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Invite;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * REST CRUD service for Invite.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("invites")
public class InviteService extends AbstractServiceCRUD<Invite, eu.lpinto.universe.api.dto.Invite, InviteController, InviteDTS> {

    @EJB
    private InviteController controller;

    public InviteService() {
        super(InviteDTS.T);
    }

    /*
     * Creates a dupplicate but is necessray for now because of the code param
     */
    @GET
    @Asynchronous
    @Produces(value = MediaType.APPLICATION_JSON)
    public void find(@Suspended final AsyncResponse asyncResponse,
                     final @Context UriInfo uriInfo,
                     final @Context HttpHeaders headers,
                     final @QueryParam(value = "code") String code,
                     final @HeaderParam(value = UniverseFundamentals.AUTH_USER_ID) Long userID) throws PreConditionException {
        Map<String, Object> options = new HashMap<>(10 + uriInfo.getQueryParameters().size());

        try {
            /* Setup */
            buildOptions(options, uriInfo, userID);

            if (code != null) {
                options.put("code", code);
            }

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
            getLogger().error(ex.getMessage(), ex);
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(forbidden((Long) options.get("user")));

        } catch (RuntimeException ex) {
            StatusEmail.sendExceptionEmail(ex, uriInfo, headers, options);
            asyncResponse.resume(noContent());
        }
    }

    @Override
    protected InviteController getController() {
        return controller;
    }
}
