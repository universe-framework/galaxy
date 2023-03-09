package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Company;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.EmployeeProfile;
import eu.lpinto.universe.persistence.entities.Invite;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.entities.Worker;
import eu.lpinto.universe.persistence.entities.WorkerProfile;
import eu.lpinto.universe.persistence.facades.EmployeeFacade;
import eu.lpinto.universe.persistence.facades.InviteFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import eu.lpinto.universe.persistence.facades.WorkerFacade;
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

    @EJB
    private EmployeeController employeeController;

    @EJB
    private EmployeeFacade employeeFacade;

    @EJB
    private WorkerFacade workerFacade;

    @EJB
    private WorkerController workerController;

    public InviteController() {
        super(Invite.class.getCanonicalName());
    }

    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, Invite invite) throws PreConditionException, UnknownIdException, PermissionDeniedException {
        String invitedEmail = invite.getEmail();

        Organization savedOrganization = organizationController.retrieve(userID, options, invite.getOrganization().getId());
        Company savedCompany = savedOrganization.getCompany();
        User savedUser = userFacade.findByEmail(invitedEmail);

        if (savedUser == null) {
            // Never seen before, create
            invite(invite, savedOrganization, userID, options);

        } else {
            // User exists
            Employee savedEmployee = employeeFacade.retrieve(savedCompany.getId(), savedUser.getId());

            if (savedEmployee == null) {
                createEmployee(savedCompany, invite, savedUser, userID, options);
                createWorker(savedOrganization, savedEmployee, savedUser, invite, userID, options);

            } else {
                // Employee already exists
                Worker savedWorker = workerFacade.retrieve(savedOrganization.getId(), savedUser.getId());

                if (savedWorker == null) {
                    createWorker(savedOrganization, savedEmployee, savedUser, invite, userID, options);

                } else {
                    throw new PreConditionException("email", "The email address already has an account at " + savedOrganization.getName());
                }
            }
        }

        /*
         * Preconditions
         */
        Invite retrieveByEmail = facade.retrieveByEmail(invitedEmail);
        if (retrieveByEmail != null) {
            facade.remove(retrieveByEmail);
        }

        invite(invite, savedOrganization, userID, options);
    }

    private void createWorker(Organization savedOrganization, Employee savedEmployee, User savedUser, Invite invite, final Long userID, final Map<String, Object> options) throws UnknownIdException, PreConditionException, PermissionDeniedException {
        Worker newWorker = new Worker(savedOrganization, savedEmployee, true, savedUser.getEmail(), invite.getRole(), savedUser.getEmail());
        workerController.doCreate(userID, options, newWorker);
    }

    private void createEmployee(Company savedCompany, Invite invite, User savedUser, final Long userID, final Map<String, Object> options) throws PreConditionException, PermissionDeniedException, UnknownIdException {
        Employee newEmployee = new Employee(savedCompany, WorkerProfile.ADMIN.equals(invite.getRole()) ? EmployeeProfile.ADMIN : EmployeeProfile.WORKER, savedUser);
        employeeController.doCreate(userID, options, newEmployee);
    }

    private void invite(Invite invite, Organization savedOrganization, final Long userID, final Map<String, Object> options) throws PreConditionException, PermissionDeniedException, UnknownIdException {
        /*
        * Body
         */
        if (invite.getName() == null) {
            invite.setName(savedOrganization.getName(), invite.getEmail());
        }

        super.doCreate(userID, options, invite);
        invite.setCode();
        super.doUpdate(userID, options, invite);

        emailController.sendInvite((String) options.get("locale"),
                                   invite.getEmail(),
                                   invite.getName(),
                                   userFacade.retrieve(userID).getName(),
                                   savedOrganization.getName(),
                                   invite.getUrl());
    }

    @Override
    public InviteFacade getFacade() {
        return facade;
    }
}
