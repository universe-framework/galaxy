package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Invite;
import eu.lpinto.universe.api.dts.InviteDTS;
import eu.lpinto.universe.controllers.InviteController;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for Invite.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("invites")
public class InviteService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Invite, Invite, InviteController, InviteDTS> {

    @EJB
    private InviteController controller;

    public InviteService() {
        super(InviteDTS.T);
    }

    @Override
    protected InviteController getController() {
        return controller;
    }
}
