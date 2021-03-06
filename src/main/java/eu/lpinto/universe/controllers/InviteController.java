package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Invite;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.InviteFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for Invite entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class InviteController extends AbstractControllerCRUD<Invite> {

    @EJB
    private UserFacade userFacade;

    @EJB
    private InviteFacade facade;

    @EJB
    private EmailController emailController;

    @EJB
    private OrganizationController organizationController;

    public InviteController() {
        super(Invite.class.getCanonicalName());
    }

    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, Invite invite) throws PreConditionException, UnknownIdException, PermissionDeniedException {
        String invitedEmail = invite.getEmail();

        if (invite.getName() == null) {
            invite.setName(invite.getOrganization().getName(), invite.getName());
        }

        /*
         * Preconditions
         */
        User savedUser = userFacade.findByEmail(invitedEmail);
        if (savedUser != null && savedUser.getEmailValidated() != null && savedUser.getEmailValidated()) {
            throw new PreConditionException("email", "The email address already has a account");
        }

        Invite retrieveByEmail = facade.retrieveByEmail(invitedEmail);
        if (retrieveByEmail != null) {
            facade.remove(retrieveByEmail);
        }

        /*
         * Body
         */
        super.doCreate(userID, options, invite);
        invite.setCode();
        super.doUpdate(userID, options, invite);

        if (invite.getOrganization() != null) {
            Organization savedOrganization = organizationController.retrieve(userID, options, invite.getOrganization().getId());
            emailController.sendInvite((String) options.get("locale"),
                                       invite.getEmail(),
                                       invite.getName(),
                                       userFacade.retrieve(userID).getName(),
                                       savedOrganization.getName(),
                                       invite.getUrl());
        }
    }

    @Override
    public InviteFacade getFacade() {
        return facade;
    }
}
