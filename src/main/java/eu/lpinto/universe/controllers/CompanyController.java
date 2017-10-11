package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnexpectedException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Company;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.EmployeeProfile;
import eu.lpinto.universe.persistence.entities.Plan;
import eu.lpinto.universe.persistence.facades.CompanyFacade;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for Company entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class CompanyController extends AbstractControllerCRUD<Company> {

    @EJB
    private CompanyFacade facade;

    @EJB
    private UserController userController;

    @EJB
    private EmployeeController employeeController;

    public CompanyController() {
        super(Company.class.getCanonicalName());
    }

    @Override
    public void doCreate(Company entity, Map<String, Object> options) throws PreConditionException, UnknownIdException, PermissionDeniedException {

        Long userID = (Long) options.get("user");

        if (entity.getPlan() == null) {
            entity.setPlan(new Plan(1l));
        }

        super.doCreate(entity, options);

        try {
            Employee employee = new Employee(entity, EmployeeProfile.ADMIN, userController.retrieve(userID, userID));
            employeeController.create(userID, employee, options);
        } catch (UnknownIdException | PermissionDeniedException ex) {
            throw new UnexpectedException("Problems creating employee for company.");
        }
    }

    @Override
    public CompanyFacade getFacade() {
        return facade;
    }
}
