package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Feature;
import eu.lpinto.universe.persistence.entities.PlanFeature;
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
    public List<Feature> find(Map<String, Object> options) {
        if (options.containsKey("organization")) {

            Long orgID = (Long) options.get("organization");

            return getEntityManager().createQuery("SELECT f FROM Feature f"
                                                  + " join PlanFeature pf ON pf.feature = f.id"
                                                  + " join Plan p ON pf.plan.id = p.id"
                                                  + " join Company c ON p.id = c.plan.id"
                                                  + " join Organization o ON c.id = o.company.id where o.id = :orgID",
                                                  Feature.class)
                    .setParameter("orgID", orgID)
                    .getResultList();
        }

        return super.find(options);
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
