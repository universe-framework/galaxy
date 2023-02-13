package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dts.InviteDTS;
import eu.lpinto.universe.controllers.InviteController;
import eu.lpinto.universe.persistence.entities.Invite;
import javax.ejb.EJB;
import javax.ws.rs.Path;

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

    @Override
    protected InviteController getController() {
        return controller;
    }
}
