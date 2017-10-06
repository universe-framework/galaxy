package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnexpectedException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.EmployeeProfile;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.Plan;
import eu.lpinto.universe.persistence.facades.OrganizationFacade;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for Organization entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class OrganizationController extends AbstractControllerCRUD<Organization> {

    @EJB
    private OrganizationFacade facade;

    @EJB
    private UserController userController;

    @EJB
    private EmployeeController employeeController;

    public OrganizationController() {
        super(Organization.class.getCanonicalName());
    }

    @Override
    public void doCreate(Organization entity, Map<String, Object> options) throws PreConditionException {

        Long userID = (Long) options.get("user");

        if (entity.getPlan() == null) {
            entity.setPlan(new Plan(1l));
        }

        super.doCreate(entity, options);

        try {
            Employee employee = new Employee(entity, EmployeeProfile.ADMIN, userController.retrieve(userID, userID));
            employeeController.create(userID, employee);
        } catch (UnknownIdException | PermissionDeniedException ex) {
            throw new UnexpectedException("Problems creating employee for organization.");
        }
    }

    @Override
    public OrganizationFacade getFacade() {
        return facade;
    }
}
