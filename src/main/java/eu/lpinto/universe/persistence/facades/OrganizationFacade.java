package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.Worker;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rita
 */
@Stateless
public class OrganizationFacade extends AbstractFacade<Organization> {

    @PersistenceContext
    private EntityManager em;

    public OrganizationFacade() {
        super(Organization.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
     * DAO
     */
    public List<Organization> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Organization> find(final Map<String, Object> options) throws PreConditionException {
        if (options.containsKey("company")) {
            return findByCompany((Long) options.get("company"), (Long) options.get("user"));
        }

        if (options.containsKey("organization") && options.containsKey("siblings") && ((Boolean) options.get("siblings"))) {
            return findBySiblings((Long) options.get("organization"), (Long) options.get("user"));
        }

        if (options.containsKey("companyOrgs")) {
            return findByUserAllInCompany((Long) options.get("user"));
        }

        return findByUser((Long) options.get("user"));
    }

    public List<Organization> findAllByUser(final Long userID) {
        return findByUser(userID);
    }

    @Override
    public Organization retrieve(Long id) throws PreConditionException {
        Organization savedOrganization = super.retrieve(id);

        savedOrganization.setWorkers(getEntityManager()
                .createQuery("select w from Worker w left join fetch w.organization"
                             + " where w.organization.id = :organizationID", Worker.class)
                .setParameter("organizationID", id)
                .getResultList());

        savedOrganization.setFull(true);

        return savedOrganization;
    }

    private List<Organization> findByUser(final Long userID) {
        return getEntityManager()
                .createQuery("SELECT o FROM Organization o WHERE o.id IN (SELECT w.organization.id FROM Worker w WHERE w.enable = true AND w.employee.user.id = :userID) AND o.enable = true", Organization.class)
                .setParameter("userID", userID)
                .getResultList();
    }

    private List<Organization> findByCompany(Long companyID, Long userID) {
        return getEntityManager()
                .createQuery("SELECT o FROM Organization o"
                             + " INNER JOIN Worker w ON w.organization.id = o.id"
                             + " WHERE o.company.id = :companyID AND o.enable=1"
                             + " AND w.employee.user.id = :userID", Organization.class)
                .setParameter("companyID", companyID)
                .setParameter("userID", userID)
                .getResultList();
    }

    private List<Organization> findBySiblings(Long id, Long userID) throws PreConditionException {
        Organization savedOrganization = super.retrieve(id);

        return getEntityManager()
                .createQuery("SELECT o FROM Organization o INNER JOIN Worker w ON w.organization.id = o.id"
                             + " WHERE o.company.id = :companyID AND o.enable=1 AND o.id <> :organizationID"
                             + "  AND w.employee.user.id = :userID", Organization.class)
                .setParameter("companyID", savedOrganization.getCompany().getId())
                .setParameter("organizationID", id)
                .setParameter("userID", userID)
                .getResultList();
    }

    private List<Organization> findByUserAllInCompany(final Long userID) {
        return getEntityManager()
                .createQuery("SELECT o FROM Organization o WHERE o.company.id IN (SELECT e.company.id FROM Employee e WHERE e.user.id = :userID) AND o.enable = true", Organization.class)
                .setParameter("userID", userID)
                .getResultList();
    }

    public Long getCompanyID(final Long id) {
        return getEntityManager()
                .createQuery("SELECT o.company.id FROM Organization o WHERE o.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
