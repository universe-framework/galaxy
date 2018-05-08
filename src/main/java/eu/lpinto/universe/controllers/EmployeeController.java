package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Company;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.EmployeeFacade;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for Employee entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class EmployeeController extends AbstractControllerCRUD<Employee> {

    @EJB
    private EmployeeFacade facade;

    @EJB
    private CompanyController companyComtroller;

    @EJB
    private UserController userComtroller;

    public EmployeeController() {
        super(Employee.class.getCanonicalName());
    }

    @Override
    public void doCreate(final Long userID, Map<String, Object> options, Employee entity) throws PreConditionException, UnknownIdException, PermissionDeniedException {
        Long companyID = entity.getCompany().getId();
        User savedUser = userComtroller.retrieve(userID, options, userID);

        Company savedCompany;
        try {
            savedCompany = companyComtroller.retrieve(userID, options, companyID);
            if (savedCompany == null) {
                throw new IllegalArgumentException("There is no Company with that id");
            }
            entity.setName(savedCompany.getName() + "_" + savedUser.getName());

            super.doCreate(userID, options, entity);

        } catch (UnknownIdException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PermissionDeniedException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public EmployeeFacade getFacade() {
        return facade;
    }
}
