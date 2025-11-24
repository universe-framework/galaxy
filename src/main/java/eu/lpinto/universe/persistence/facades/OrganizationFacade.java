package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Organization;
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
    @Override
    public List<Organization> find(final Map<String, Object> options) throws PreConditionException {
        if(options.containsKey("company")) {
            return findByCompanyAndUser((Long) options.get("company"), (Long) options.get("user"));

        } else if(options.containsKey("organization") && options.containsKey("siblings") && ((Boolean) options.get("siblings"))) {
            return findBySiblings((Long) options.get("organization"), (Long) options.get("user"));

        } else if(options.containsKey("companyOrgs")) {
            return findByUserAllInCompany((Long) options.get("user"));

        } else if(options.containsKey("god")) {
            if(options.containsKey("enable") && ((Boolean) options.get("enable"))) {
                return findAllForGOD();

            } else {
                return findForGOD();
            }

        } else if(options.containsKey("remoteAddr")) {
            return findByUserIp((Long) options.get("user"), (String) options.get("remoteAddr"));

        } else {
            return findByUser((Long) options.get("user"));
        }
    }

    public List<Organization> findAllByUser(final Long userID) {
        return findByUser(userID);
    }

    public Organization retrieveAndFilterIP(Long id, String remoteAddr) throws PreConditionException {
        try {
            return getEntityManager()
                    .createQuery("SELECT o"
                                 + " FROM Organization o"
                                 + " WHERE o.id = :organizationID"
                                 + " AND o.enable IS TRUE"
                                 + " AND (o.ip IS NULL OR o.ip = :remoteAddr)",
                                 Organization.class)
                    .setParameter("organizationID", id)
                    .setParameter("remoteAddr", remoteAddr)
                    .getSingleResult();
        } catch(NoResultException ex) {
            throw new PreConditionException("Organization", "has IP Control");
        }
    }

    private List<Organization> findForGOD() {
        return getEntityManager()
                .createQuery("SELECT o"
                             + " FROM Organization o"
                             + " WHERE o.enable = true", Organization.class)
                .getResultList();
    }

    private List<Organization> findAllForGOD() {
        return getEntityManager()
                .createQuery("SELECT o"
                             + " FROM Organization o"
                             + " WHERE o.deleted IS NULL", Organization.class)
                .getResultList();
    }

    private List<Organization> findByUser(final Long userID) {
        return getEntityManager()
                .createQuery("SELECT o FROM Organization o WHERE o.id IN (SELECT w.organization.id FROM Worker w WHERE w.enable = true AND w.employee.user.id = :userID) AND o.enable = true", Organization.class)
                .setParameter("userID", userID)
                .getResultList();
    }

    private List<Organization> findByUserIp(final Long userID, final String remoteAddr) {
        return getEntityManager()
                .createQuery("SELECT o "
                             + "FROM Organization o"
                             + " WHERE o.id IN (SELECT w.organization.id FROM Worker w WHERE w.enable = true AND w.employee.user.id = :userID)"
                             + " AND o.enable = true"
                             + " AND (o.ip is null OR o.ip = :remoteAddr)", Organization.class)
                .setParameter("userID", userID)
                .setParameter("remoteAddr", remoteAddr)
                .getResultList();
    }

    private List<Organization> findByCompanyAndUser(Long companyID, Long userID) {
        return getEntityManager()
                .createQuery("SELECT o FROM Organization o"
                             + " INNER JOIN Worker w ON w.organization.id = o.id"
                             + " WHERE o.company.id = :companyID"
                             + " AND o.enable = 1"
                             + " AND w.employee.user.id = :userID", Organization.class)
                .setParameter("companyID", companyID)
                .setParameter("userID", userID)
                .getResultList();
    }

    private List<Organization> findBySiblings(Long id, Long userID) throws PreConditionException {
        Organization savedOrganization = super.retrieve(id);

        if(savedOrganization == null || savedOrganization.getCompany() == null) {
            return null;
        }

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
                .createQuery("SELECT o FROM Organization o"
                             + " WHERE o.company.id IN (SELECT e.company.id FROM Employee e WHERE e.user.id = :userID)"
                             + " AND o.enable is true", Organization.class)
                .setParameter("userID", userID)
                .getResultList();
    }

    public List<Organization> findByCustomField(final String customField) {
        return getEntityManager().createQuery("SELECT o"
                                              + " FROM Organization o"
                                              + " WHERE o.customField = :customField", Organization.class)
                .setParameter("customField", customField)
                .getResultList();
    }

    public List<Organization> getCompany(final Long companyID) {
        return getEntityManager()
                .createQuery("SELECT o"
                             + " FROM Organization o"
                             + " WHERE o.enable is true"
                             + " AND o.company.id = :id", Organization.class)
                .setParameter("id", companyID)
                .getResultList();
    }

    public Boolean hasFeature(final Long organizationID, final Long featureID) {
        try {
            return getEntityManager().createNativeQuery("SELECT o.id FROM Organization_Feature o"
                                                        + " WHERE o.organization_id = :organizationID"
                                                        + " AND o.feature_id = :featureID")
                    .setParameter("organizationID", organizationID)
                    .setParameter("featureID", featureID)
                    .getSingleResult() != null;
        } catch(NoResultException ex) {
            return false;
        }
    }

    public Boolean hasFeatureSales(final Long organizationID) {
        try {
            return getEntityManager().createNativeQuery("SELECT o.id FROM Organization_Feature o"
                                                        + " WHERE o.organization_id = :organizationID"
                                                        + " AND o.feature_id = :featureID")
                    .setParameter("organizationID", organizationID)
                    .setParameter("featureID", 4)
                    .getSingleResult() != null;
        } catch(NoResultException ex) {
            return false;
        }
    }

    public Boolean hasSetting(final Long organizationID, final String code) {
        try {
            return getEntityManager().createNativeQuery(
                    "SELECT osb.id"
                    + " FROM Organization_SettingBoolean osb"
                    + " INNER JOIN Setting s ON s.id = osb.setting_id"
                    + " WHERE s.code = :code"
                    + " AND osb.organization_id = :organizationID")
                    .setParameter("organizationID", organizationID)
                    .setParameter("code", code)
                    .getSingleResult() != null;
        } catch(NoResultException ex) {
            return false;
        }
    }

    public Long getCompanyID(final Long id) {
        try {
            return getEntityManager()
                    .createQuery("SELECT o.company.id FROM Organization o WHERE o.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch(NoResultException ex) {
            return null;
        }
    }
}
