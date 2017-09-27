package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Feature;
import eu.lpinto.universe.persistence.entities.PlanFeature;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
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
    public Feature retrieve(Long id) {
        Feature result = super.retrieve(id);

        result.setPlans(getEntityManager()
                .createQuery("select e from PlanFeature e left join fetch e.feature"
                             + " where e.feature.id = :id", PlanFeature.class).setParameter("id", id).getResultList());

        result.setFull(true);

        return result;
    }
}
