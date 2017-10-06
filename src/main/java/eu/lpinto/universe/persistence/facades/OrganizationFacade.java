package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Organization;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA facade for Organization entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class OrganizationFacade extends AbstractFacade<Organization> {

    @PersistenceContext
    private EntityManager em;

    public OrganizationFacade() {
        super(Organization.class);
    }

    @Override
    public List<Organization> find(Map<String, Object> options) {
        Long userID = (Long) options.get("user");

        return getEntityManager()
                .createQuery("select o from Organization o WHERE o.id IN (SELECT e.organization.id from Employee e WHERE e.user.id = :userID)", Organization.class)
                .setParameter("userID", userID)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
