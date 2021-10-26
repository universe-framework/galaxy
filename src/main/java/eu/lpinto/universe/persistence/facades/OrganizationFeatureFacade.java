package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.OrganizationFeature;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
@Stateless
public class OrganizationFeatureFacade extends AbstractFacade<OrganizationFeature> {

    @PersistenceContext
    private EntityManager em;

    public OrganizationFeatureFacade() {
        super(OrganizationFeature.class);
    }

    @Override
    public List<OrganizationFeature> find(Map<String, Object> options) throws PreConditionException {
        if (options.containsKey(("organization"))) {
            return getByOrganization((Long) options.get(("organization")));
        }

        throw new AssertionError("Cannot list all " + getEntityClass().getSimpleName() + ". Please report this!");
    }

    public List<OrganizationFeature> getByOrganization(final Long organizationID) {
        return em.createQuery(
                "SELECT t FROM OrganizationFeature t"
                + " WHERE t.organization.id = :organizationID"
                + " AND t.deleted IS NULL", OrganizationFeature.class)
                .setParameter("organizationID", organizationID)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
