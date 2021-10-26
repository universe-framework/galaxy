package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Feature;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Feature Facade
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class FeatureFacade extends AbstractFacade<Feature> {

    @PersistenceContext
    private EntityManager em;

    public FeatureFacade() {
        super(Feature.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<Feature> find(Map<String, Object> options) throws PreConditionException {
        if (options.containsKey("organization")) {

            Long orgID = (Long) options.get("organization");

            return getEntityManager().createQuery("SELECT t.feature FROM OrganizationFeature t"
                                                  + " WHERE t.organization.id = :orgID"
                                                  + " AND t.deleted IS NULL",
                                                  Feature.class)
                    .setParameter("orgID", orgID)
                    .getResultList();
        }

        return findAll();
    }
}
