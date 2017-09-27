package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Plan;
import eu.lpinto.universe.persistence.entities.PlanFeature;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Plan Facade
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class PlanFacade extends AbstractFacade<Plan> {

    @PersistenceContext
    private EntityManager em;

    public PlanFacade() {
        super(Plan.class);
    }

    /*
     * DAO
     */
    @Override
    public Plan retrieve(Long id) {
        Plan result = super.retrieve(id);

        result.setFeatures(getEntityManager()
                .createQuery("select e from PlanFeature e left join fetch e.plan"
                             + " where e.plan.id = :id", PlanFeature.class).setParameter("id", id).getResultList());

        result.setFull(true);

        return result;
    }

    /*
     * Getters/Setters
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
