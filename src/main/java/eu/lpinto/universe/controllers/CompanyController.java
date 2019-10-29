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
import eu.lpinto.universe.util.UniverseFundamentals;
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
    public void doCreate(final Long userID, final Map<String, Object> options, Company entity) throws PreConditionException, UnknownIdException, PermissionDeniedException {

        if (entity.getPlan() == null) {
            entity.setPlan(new Plan(Long.valueOf(UniverseFundamentals.DEFAULT_PLAN)));
        }

        super.doCreate(userID, options, entity);

        try {
            Employee employee = new Employee(entity, EmployeeProfile.ADMIN, userController.retrieve(userID, options, userID));
            employeeController.create(userID, options, employee);
        } catch (UnknownIdException | PermissionDeniedException ex) {
            throw new UnexpectedException("Problems creating employee for company.");
        }
    }

    @Override
    public CompanyFacade getFacade() {
        return facade;
    }
}
