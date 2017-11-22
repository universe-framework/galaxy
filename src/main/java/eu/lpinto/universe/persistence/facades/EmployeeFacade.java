package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.api.dto.Person;
import eu.lpinto.universe.persistence.entities.Company;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.EmployeeProfile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * EJB facade for shelter-people relation.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class EmployeeFacade extends AbstractFacade<Employee> {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private CompanyFacade companyFacade;

    /*
     * Constructors
     */
    public EmployeeFacade() {
        super(Employee.class);
    }

    /*
     * CRUD
     */
    @Override
    public List<Employee> find(Map<String, Object> options) {
        if (options != null) {
            if (options.containsKey("company")) {
                if (options.containsKey("externalID")) {
                    return getByCompanyAndExternalID((Long) options.get("company"), options.get("externalID").toString());

                } else if (options.containsKey("profile")) {
                    return getByCompanyAndProfiles((Long) options.get("company"), options.get("profile").toString());

                } else if (options.containsKey("user")) {
                    return getByCompanyAndUser((Long) options.get("company"), (Long) options.get("user"));

                } else {
                    return getByCompany((Long) options.get("company"));
                }
            }
        }

        return super.findAll();
    }

    public Employee retrieve(final Long companyID, final Long personID) {
        try {
            Employee companyPeople = getEntityManager()
                    .createQuery("SELECT employee FROM Employee employee "
                                 + "WHERE employee.company.id = :companyID AND employee.person.id = :personID", Employee.class)
                    .setParameter("companyID", companyID)
                    .setParameter("PpersonID", personID)
                    .getSingleResult();

            return companyPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void edit(final Employee newEmployee
    ) {
        Employee savedEmployee = new Employee();
        Company company = new Company();
        Person person = new Person();

        if (newEmployee.getCompany() != null) {
            if (newEmployee.getCompany().getId() != null) {
                Long companyID = newEmployee.getCompany().getId();
                company = companyFacade.retrieve(companyID);
            }
        }

        if (newEmployee.getId() != null) {
            savedEmployee = retrieve(newEmployee.getId());
        }

        if (savedEmployee == null) {
            throw new IllegalArgumentException("There is no Employee with that id");
        }
        if (company == null) {
            throw new IllegalArgumentException("There is no Company with that id");
        }
        if (person == null) {
            throw new IllegalArgumentException("There is no Person with that id");
        }

        if (savedEmployee.getCompany() != null) {
            newEmployee.setCompany(savedEmployee.getCompany());
        }

        //The code below is to get the name of the employee with that id since the name doesnt come down
        if (savedEmployee.getName() != null) {
            String name = savedEmployee.getName();
            newEmployee.setName(name);
        }

        super.edit(newEmployee);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
     * Helpers
     */
    private List<Employee> getByCompany(final Long companyID) {
        try {
            List<Employee> companyPeople = getEntityManager().createQuery("SELECT employee FROM Employee employee WHERE employee.company.id = :companyID", Employee.class)
                    .setParameter("companyID", companyID)
                    .getResultList();

            return companyPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    private List<Employee> getByCompanyAndProfiles(final Long companyID, final String profile) {
        List<EmployeeProfile> profiles;
        if ("staff".equals(profile)) {
            profiles = Arrays.asList(EmployeeProfile.ADMIN, EmployeeProfile.WORKER);

        } else {
            profiles = new ArrayList<>(0);
        }

        try {
            List<Employee> companyPeople = getEntityManager().createQuery("SELECT employee FROM Employee employee WHERE  employee.company.id = :companyID AND employee.profile in :profiles", Employee.class)
                    .setParameter("companyID", companyID)
                    .setParameter("profiles", profiles)
                    .getResultList();

            return companyPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    private List<Employee> getByCompanyAndUser(final Long companyID, final Long userID) {
        try {
            List<Employee> companyPeople = getEntityManager()
                    .createQuery("SELECT employee FROM Employee employee"
                                 + " WHERE employee.company.id = :companyID AND employee.user.id = :userID", Employee.class)
                    .setParameter("companyID", companyID)
                    .setParameter("userID", userID)
                    .getResultList();

            return companyPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<Employee> getByCompanyAndExternalID(final Long companyID, final String externalID) {
        try {
            List<Employee> companyPeople = getEntityManager().createQuery("SELECT employee FROM Employee employee WHERE (employee.externalID = :externalID AND employee.company.id = :companyID)", Employee.class)
                    .setParameter("companyID", companyID)
                    .setParameter("externalID", externalID)
                    .getResultList();

            return companyPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }
}
