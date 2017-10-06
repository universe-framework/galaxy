package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.EmployeeProfile;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.Person;
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
    private OrganizationFacade organizationFacade;

    @EJB
    private PersonFacade personFacade;

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
            if (options.containsKey("organization")) {
                if (options.containsKey("externalID")) {
                    return getByOrganizationAndExternalID((Long) options.get("organization"), options.get("externalID").toString());

                } else if (options.containsKey("profile")) {
                    return getByOrganizationAndProfiles((Long) options.get("organization"), options.get("profile").toString());

                } else if (options.containsKey("user")) {
                    return getByOrganizationAndUser((Long) options.get("organization"), (Long) options.get("user"));

                } else {
                    return getByOrganization((Long) options.get("organization"));
                }
            }
        }

        return super.findAll();
    }

    public Employee retrieve(final Long organizationID, final Long personID) {
        try {
            Employee organizationPeople = getEntityManager()
                    .createQuery("SELECT employee FROM Employee employee "
                                 + "WHERE employee.organization.id = :organizationID AND employee.person.id = :personID", Employee.class)
                    .setParameter("organizationID", organizationID)
                    .setParameter("PpersonID", personID)
                    .getSingleResult();

            return organizationPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void create(Employee entity) {
        Long organizationID = entity.getOrganization().getId();

        Organization savedOrganization = organizationFacade.retrieve(organizationID);
        if (savedOrganization == null) {
            throw new IllegalArgumentException("There is no Organization with that id");
        }

        entity.setName(savedOrganization.getName() + "_" + entity.getName());

        super.create(entity);
    }

    @Override
    public void edit(final Employee newEmployee
    ) {
        Employee savedEmployee = new Employee();
        Organization organization = new Organization();
        Person person = new Person();

        if (newEmployee.getOrganization() != null) {
            if (newEmployee.getOrganization().getId() != null) {
                Long organizationID = newEmployee.getOrganization().getId();
                organization = organizationFacade.retrieve(organizationID);
            }
        }

        if (newEmployee.getId() != null) {
            savedEmployee = retrieve(newEmployee.getId());
        }

        if (savedEmployee == null) {
            throw new IllegalArgumentException("There is no Employee with that id");
        }
        if (organization == null) {
            throw new IllegalArgumentException("There is no Organization with that id");
        }
        if (person == null) {
            throw new IllegalArgumentException("There is no Person with that id");
        }

        if (savedEmployee.getOrganization() != null) {
            newEmployee.setOrganization(savedEmployee.getOrganization());
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
    private List<Employee> getByOrganization(final Long organizationID) {
        try {
            List<Employee> organizationPeople = getEntityManager().createQuery("SELECT employee FROM Employee employee WHERE employee.organization.id = :organizationID", Employee.class)
                    .setParameter("organizationID", organizationID)
                    .getResultList();

            return organizationPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    private List<Employee> getByOrganizationAndProfiles(final Long organizationID, final String profile) {
        List<EmployeeProfile> profiles;
        if ("staff".equals(profile)) {
            profiles = Arrays.asList(EmployeeProfile.ADMIN, EmployeeProfile.WORKER);

        } else {
            profiles = new ArrayList<>(0);
        }

        try {
            List<Employee> organizationPeople = getEntityManager().createQuery("SELECT employee FROM Employee employee WHERE  employee.organization.id = :organizationID AND employee.profile in :profiles", Employee.class)
                    .setParameter("organizationID", organizationID)
                    .setParameter("profiles", profiles)
                    .getResultList();

            return organizationPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    private List<Employee> getByOrganizationAndUser(final Long organizationID, final Long userID) {
        try {
            List<Employee> organizationPeople = getEntityManager()
                    .createQuery("SELECT employee FROM Employee employee"
                                 + " WHERE employee.organization.id = :organizationID AND employee.user.id = :userID", Employee.class)
                    .setParameter("organizationID", organizationID)
                    .setParameter("userID", userID)
                    .getResultList();

            return organizationPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<Employee> getByOrganizationAndExternalID(final Long organizationID, final String externalID) {
        try {
            List<Employee> organizationPeople = getEntityManager().createQuery("SELECT employee FROM Employee employee WHERE (employee.externalID = :externalID AND employee.organization.id = :organizationID)", Employee.class)
                    .setParameter("organizationID", organizationID)
                    .setParameter("externalID", externalID)
                    .getResultList();

            return organizationPeople;
        } catch (NoResultException ex) {
            return null;
        }
    }
}
