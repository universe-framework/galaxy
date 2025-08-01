package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.*;
import eu.lpinto.universe.persistence.facades.*;
import java.util.List;
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

    @EJB
    private OrganizationFeatureFacade organizationFeatureFacade;

    public InviteController() {
        super(Invite.class.getCanonicalName());
    }

    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, Invite invite) throws PreConditionException, UnknownIdException, PermissionDeniedException {
        String invitedEmail = invite.getEmail();

        Organization savedOrganization = organizationController.retrieve(userID, options, invite.getOrganization().getId());
        Company savedCompany = savedOrganization.getCompany();
        User savedUser = userFacade.findByEmail(invitedEmail);

        Long nrVeterinarians = workerFacade.countByRole(invite.getRole(), invite.getOrganization().getId());
        List<OrganizationFeature> features = organizationFeatureFacade.getByOrganization(invite.getOrganization().getId());

        Float clinicalFeatureQuantity = 0F;

        for(OrganizationFeature feature : features) {
            if(feature.getFeature().getId() == 3) {
                clinicalFeatureQuantity = feature.getQuantity();
            }
        }

        if(nrVeterinarians >= clinicalFeatureQuantity) {
            throw new PreConditionException("Modules: Clinical", "already full of veterinarians!");
        }

        if(savedUser == null) {
            List<Worker> savedWorkers = workerFacade.findByOrganizationAndEmail(invite.getOrganization().getId(), invitedEmail);
            if(savedWorkers == null || savedWorkers.isEmpty()) {

                // Never seen before, create
                Invite retrieveByEmail = facade.retrieveByEmail(invitedEmail);
                if(retrieveByEmail != null) {
                    facade.remove(retrieveByEmail);
                }

                invite(invite, savedOrganization, userID, options);
            }

        } else {
            // User exists
            Employee savedEmployee = employeeFacade.retrieve(savedCompany.getId(), savedUser.getId());

            if(savedEmployee == null) {
                savedEmployee = createEmployee(savedCompany, invite, savedUser, userID, options);
                createWorker(savedOrganization, savedEmployee, savedUser, invite, userID, options);

            } else {
                // Employee already exists
                Worker savedWorker = workerFacade.retrieve(savedOrganization.getId(), savedUser.getId());

                if(savedWorker == null) {
                    createWorker(savedOrganization, savedEmployee, savedUser, invite, userID, options);

                } else {
                    throw new PreConditionException("email", "The email address already has an account at " + savedOrganization.getName());
                }
            }
        }
    }

    private void createWorker(Organization savedOrganization, Employee savedEmployee, User savedUser, Invite invite, final Long userID, final Map<String, Object> options) throws UnknownIdException, PreConditionException, PermissionDeniedException {
        Worker newWorker = new Worker(savedOrganization, savedEmployee, true, savedUser.getEmail(), invite.getRole(), savedUser.getName());
        workerController.doCreate(userID, options, newWorker);
    }

    private Employee createEmployee(Company savedCompany, Invite invite, User savedUser, final Long userID, final Map<String, Object> options) throws PreConditionException, PermissionDeniedException, UnknownIdException {
        Employee newEmployee = new Employee(savedCompany, WorkerProfile.ADMIN.equals(invite.getRole()) ? EmployeeProfile.ADMIN : EmployeeProfile.WORKER, savedUser);
        employeeController.doCreate(userID, options, newEmployee);
        return newEmployee;
    }

    private void invite(Invite invite, Organization savedOrganization, final Long userID, final Map<String, Object> options) throws PreConditionException, PermissionDeniedException, UnknownIdException {
        /*
         * Body
         */
        if(invite.getName() == null) {
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
