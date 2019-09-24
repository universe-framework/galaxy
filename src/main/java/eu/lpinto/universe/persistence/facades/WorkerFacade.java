package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Worker;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rita
 */
@Stateless
public class WorkerFacade extends AbstractFacade<Worker> {

    @PersistenceContext
    private EntityManager em;

    public WorkerFacade() {
        super(Worker.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Worker> findAll() {
        try {
            List<Worker> clinics = getEntityManager().createQuery(
                    "SELECT w FROM Worker w WHERE w.enable = true", Worker.class)
                    .getResultList();

            return clinics;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Worker> find(final Map<String, Object> options) throws PreConditionException {
        if (options.containsKey("organization")) {
            Long organizationID = (Long) options.get("organization");

            if (options.containsKey("me")) {

                /*
                 * By Organization & User
                 */
                Long userID = (Long) options.get("user");
                return findByOrganizationAndUser(organizationID, userID);

            } else {

                /*
                 * By Organization
                 */
                return findByOrganization(organizationID);
            }
        }

        return super.find(null);
    }

    private List<Worker> findByOrganization(final Long organizationID) {
        List<Worker> workers = getEntityManager().createQuery(
                "SELECT w FROM Worker w WHERE w.organization.id = :organizationID AND w.enable = true", Worker.class)
                .setParameter("organizationID", organizationID)
                .getResultList();

        return workers;
    }

    private List<Worker> findByOrganizationAndUser(final Long organizationID, final Long userID) {
        List<Worker> workers = getEntityManager().createQuery(
                "SELECT w FROM Worker w WHERE w.organization.id = :organizationID AND w.enable = true AND w.employee.user.id = :userID", Worker.class)
                .setParameter("organizationID", organizationID)
                .setParameter("userID", userID)
                .getResultList();

        return workers;
    }

    public Worker getByOrganizationAndUser(final Long organizationID, final Long userID) {
        try {
            return getEntityManager().createQuery(
                    "SELECT w FROM Worker w WHERE w.organization.id = :organizationID AND w.enable = true AND w.employee.user.id = :userID", Worker.class)
                    .setParameter("organizationID", organizationID)
                    .setParameter("userID", userID)
                    .setMaxResults(1)
                    .getSingleResult();

        } catch (NoResultException ex) {
            return null;
        }
    }
}
