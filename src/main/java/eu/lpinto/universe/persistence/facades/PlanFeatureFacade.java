package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Feature;
import eu.lpinto.universe.persistence.entities.Plan;
import eu.lpinto.universe.persistence.entities.PlanFeature;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * EJB facade for Plan-Feature relation.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class PlanFeatureFacade extends AbstractFacade<PlanFeature> {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private PlanFacade planFacade;

    @EJB
    private FeatureFacade featureFacade;

    /*
     * Constructors
     */
    public PlanFeatureFacade() {
        super(PlanFeature.class);
    }

    /*
     * CRUD
     */
    @Override
    public List<PlanFeature> find(final Map<String, Object> options) {
        if (options == null) {
            return super.find(null);

        } else {
            if (options.containsKey("entity")) {
                PlanFeature entity = options.get("entity") == null ? null : (PlanFeature) options.get("entity");
                if (entity == null) {
                    throw new AssertionError("Cannot find for [null] entity. Maybe you want to call findAll().");

                }
                if (entity.getPlan() != null && entity.getPlan().getId() != null) {
                    return getByPlan(entity.getPlan().getId());
                }
            }
        }

        throw new IllegalArgumentException();
    }

    @Override
    public PlanFeature retrieve(Long id) {
        PlanFeature result = super.retrieve(id);

        result.setPlan(getEntityManager()
                .createQuery("select e from Plan e left join fetch e.features"
                             + " where e.features.id = :id", Plan.class).setParameter("id", id).getSingleResult());

        result.setFeature(getEntityManager()
                .createQuery("select e from Feature e left join fetch e.plans"
                             + " where e.plans.id = :id", Feature.class).setParameter("id", id).getSingleResult());

        result.setFull(true);

        return result;
    }

    @Override
    public void create(PlanFeature entity) {

        if (entity.getPlan() == null) {
            throw new IllegalArgumentException("Plan cannot be null");
        }
        if (entity.getPlan().getId() == null) {
            throw new IllegalArgumentException("Plan ID cannot be null");
        }

        if (entity.getFeature() == null) {
            throw new IllegalArgumentException("Feature cannot be null");
        }
        if (entity.getFeature().getId() == null) {
            throw new IllegalArgumentException("Feature ID cannot be null");
        }

        Long planID = entity.getPlan().getId();
        Long featureID = entity.getFeature().getId();

        Plan savedPlan = planFacade.retrieve(planID);
        Feature savedFeature = featureFacade.retrieve(featureID);

        if (savedPlan == null) {
            throw new IllegalArgumentException("There is no Plan with that id");
        }

        if (savedFeature == null) {
            throw new IllegalArgumentException("There is no Feature with that id");
        }

        if (savedPlan.getName() != null && savedFeature.getName() != null) {
            entity.setName(savedPlan.getName() + " - " + savedFeature.getName());
        }

        super.create(entity);
    }

    @Override
    public void edit(final PlanFeature newPlanFeature) {

        Plan plan = newPlanFeature.getPlan();
        Feature feature = newPlanFeature.getFeature();

        if (newPlanFeature.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        if (plan == null) {
            throw new IllegalArgumentException("Plan cannot be null");
        }
        if (feature == null) {
            throw new IllegalArgumentException("Feature cannot be null");
        }
        Plan savedPlan = planFacade.retrieve(plan.getId());

        if (savedPlan == null) {
            throw new IllegalArgumentException("There is no Plan with that id");
        }
        newPlanFeature.setPlan(savedPlan);

        Feature savedFeature = featureFacade.retrieve(feature.getId());

        if (savedFeature == null) {
            throw new IllegalArgumentException("There is no Feature with that id");
        }
        newPlanFeature.setFeature(savedFeature);

        PlanFeature savedPlanFeature = retrieve(newPlanFeature.getId());

        if (savedPlanFeature != null && savedPlanFeature.getName() != null) {
            newPlanFeature.setName(savedPlanFeature.getName());
        }

        super.edit(newPlanFeature);

    }

    /*
     * Override
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
     * Helpers
     */
    private List<PlanFeature> getByPlan(final Long planID) {
        try {
            TypedQuery<PlanFeature> query = getEntityManager().createQuery(
                    "SELECT p FROM PlanFeature p WHERE p.plan.id = :planID", PlanFeature.class);
            return query.setParameter("planID", planID).getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
