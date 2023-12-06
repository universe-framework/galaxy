package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * JPA facade for User entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext
    private EntityManager em;

    public UserFacade() {
        super(User.class);
    }

    @Override
    public List<User> find(final Map<String, Object> options) {

        if (options.containsKey("company")) {
            return getByCompany((Long) options.get("company"));

        } else if (options.containsKey("hasClinic")) {
            return getByHasClinic((Boolean) options.get("hasClinic"));
        }

        throw new AssertionError("Cannot list all " + getEntityClass().getSimpleName() + ". Please report this!");
    }

    /*
     * DAO
     */
    public User findByName(final String name) {
        try {
            return (User) em.createNamedQuery("User.findByName").setParameter("name", name).getSingleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    public User findByEmail(final String email) {
        try {
            return (User) em.createNamedQuery("User.findByEmail").setParameter("email", email).getSingleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
     * Helpers
     */
    private List<User> getByHasClinic(final Boolean hasClinic) {
        try {
            List<User> users = new ArrayList();

            if (hasClinic == false) {
                users = getEntityManager().createQuery(
                        "SELECT u FROM User u WHERE (u.person.clinicJobs IS EMPTY AND u.person.shelters IS EMPTY)", User.class)
                        .getResultList();
            } else {
                users = getEntityManager().createQuery(
                        "SELECT u FROM User u WHERE (u.person.clinicJobs IS NOT EMPTY AND u.person.shelters IS NOT EMPTY)", User.class)
                        .getResultList();
            }

            return users;
        } catch(NoResultException ex) {
            return null;
        }
    }

    public User getbyToken(final String token) {
        try {
            return getEntityManager().createQuery(
                    "SELECT new User(u.id, u.god) FROM User u INNER JOIN Token t ON t.user.id = u.id WHERE t.token = :token", User.class)
                    .setParameter("token", token)
                    .getSingleResult();

        } catch(NoResultException ex) {
            return null;
        }
    }

    public List<User> getByCompany(final Long companyID) {
        return getEntityManager().createNativeQuery(
                "SELECT u.* FROM ApplicationUser u INNER JOIN Employee e ON e.user_id = u.id AND e.company_id = :companyID", User.class)
                .setParameter("companyID", companyID)
                .getResultList();
    }

    public List<User> getByCompanyX(final Long companyID) {
        return getEntityManager().createQuery(
                "SELECT e.user FROM Employee e WHERE e.company.id = :companyID", User.class)
                .setParameter("companyID", companyID)
                .getResultList();
    }

    @Override
    public void remove(User entity) {
        throw new IllegalArgumentException("Users cannot be deleted");

    }
}
