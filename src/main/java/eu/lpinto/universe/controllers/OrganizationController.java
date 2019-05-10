package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnexpectedException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.*;
import eu.lpinto.universe.persistence.facades.CompanyFacade;
import eu.lpinto.universe.persistence.facades.OrganizationFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import eu.lpinto.universe.persistence.facades.WorkerFacade;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Rita
 */
@Stateless
public class OrganizationController extends AbstractControllerCRUD<Organization> {

    private static final Boolean ENABLE_DEFAULT = true;
    private static final Boolean WORKER_ENABLE_DEFAULT = true;

    @EJB
    private OrganizationFacade facade;

    @EJB
    private CompanyFacade companyFacade;

    @EJB
    private UserFacade userFacade;

    //Not Used
    /* @EJB
     * private EmployeeFacade employeeFacade; */
    @EJB
    private WorkerFacade workerFacade;

    //Not Used
    /* @EJB
     * private PMSClinicFacade pmsFacade; */

 /*
     * Controllers
     */
    public OrganizationController() {
        super(Organization.class.getCanonicalName());
    }

    /*
     * Public methods
     */
    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, Organization newOrganization) throws PreConditionException, UnknownIdException, PermissionDeniedException {

        /*
         * Preconditions
         */
        final String key = "user";
        if (false == options.containsKey(key)) {
            throw new UnexpectedException("Missing userID");
        }

        if (newOrganization.getCompany() == null || newOrganization.getCompany().getId() == null) {
            throw new UnexpectedException("Missing company id");
        }

        /*
         * Method Body
         */
        Company savedCompany = companyFacade.retrieve(newOrganization.getCompany().getId());

        if (savedCompany == null) {
            throw new PreConditionException("company", "unknown");
        }

        List<Employee> employees = savedCompany.getEmployees();

        Employee currentEmployee = null;

        for (Employee employee : employees) {
            if (userID.equals(employee.getUser().getId())) {
                currentEmployee = employee;
            }
        }

        if (currentEmployee == null) {
            throw new PreConditionException("organization.company", "User is not a employee");
        }

        User user = userFacade.retrieve(userID);

        /*
         * Creates a new Hopi Organization
         */
        if (newOrganization instanceof UniverseEntity) {
            Organization organizationEntity = (Organization) newOrganization;

            Calendar newNow = options.get("now") == null ? new GregorianCalendar() : (Calendar) options.get("now");
            organizationEntity.setCreated(newNow);
            organizationEntity.setUpdated(newNow);
        }

        newOrganization.setEnable(ENABLE_DEFAULT);
        newOrganization.setCompany(currentEmployee.getCompany());
        super.doCreate(userID, options, newOrganization);

        Worker worker = new Worker();
        worker.setName(currentEmployee.getUser().getName());
        worker.setEmail(user.getEmail());
        worker.setRole(WorkerProfile.ADMIN);
        worker.setEnable(WORKER_ENABLE_DEFAULT);
        worker.setOrganization(newOrganization);
        worker.setEmployee(currentEmployee);
        workerFacade.create(worker);
    }

    @Override
    public void doUpdate(final Long userID, final Map<String, Object> options, Organization newClinic) {

        try {
            Organization savedClinic = super.doRetrieve(userID, options, newClinic.getId());

            if (savedClinic.getWorkers() != null && !savedClinic.getWorkers().isEmpty()) {
                newClinic.setWorkers(savedClinic.getWorkers());
            }
//            if (savedClinic.getPatients() != null && !savedClinic.getPatients().isEmpty()) {
//                newClinic.setPatients(savedClinic.getPatients());
//            }

            //Hopi
            newClinic.setExternalID(savedClinic.getExternalID());
            newClinic.setEnable(true);
            newClinic.setCompany(savedClinic.getCompany());
            super.doUpdate(userID, options, newClinic);

        } catch (UnknownIdException | PreConditionException ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void doDelete(final Long userID, final Map<String, Object> options, Organization savedClinic) throws PreConditionException {
        try {
            Organization newClinic = super.doRetrieve(userID, options, savedClinic.getId());
            savedClinic.setExternalID(newClinic.getExternalID());
            savedClinic.setEnable(false);
            savedClinic.setCompany(newClinic.getCompany());
            super.doUpdate(userID, options, savedClinic);
        } catch (UnknownIdException ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Helpers
     */
    @Override
    public Boolean assertPremissionsCreate(Long userID, Organization entity) throws PermissionDeniedException {
        if (entity.getId() == null && userID != null) {
            return true;
        }
        throw new PermissionDeniedException();
    }

    @Override
    public Boolean assertPremissionsRead(Long userID, Organization entity) throws PermissionDeniedException {
        return true;
    }

    @Override
    public Boolean assertPremissionsUpdateDelete(Long userID, Organization entity) throws PermissionDeniedException {
        try {
            Organization savedClinic = super.doRetrieve(userID, new HashMap<>(0), entity.getId());
            if (savedClinic.getEnable() == true) {
                return true;
            } else {
                throw new PermissionDeniedException();
            }
        } catch (UnknownIdException | PreConditionException ex) {
            Logger.getLogger(OrganizationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /*
     * Getters / Setters
     */
    @Override
    public OrganizationFacade getFacade() {
        return facade;
    }

}
