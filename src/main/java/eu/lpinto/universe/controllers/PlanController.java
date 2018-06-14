package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Plan;
import eu.lpinto.universe.persistence.facades.PlanFacade;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class PlanController extends AbstractControllerCRUD<Plan> {

    @EJB
    private PlanFacade facade;

    public PlanController() {
        super(Plan.class.getCanonicalName());
    }

    @Override
    public List<Plan> doFind(Long userID, Map<String, Object> options) throws PreConditionException {
        List<Plan> doFindAll = super.doFind(userID, options);

        if (doFindAll == null || doFindAll.isEmpty()) {
            Calendar now = Calendar.getInstance();
            eu.lpinto.universe.persistence.entities.Plan p1 = new eu.lpinto.universe.persistence.entities.Plan(null, null, "Free", now, now);
            getFacade().create(p1);

            eu.lpinto.universe.persistence.entities.Plan p2 = new eu.lpinto.universe.persistence.entities.Plan(null, null, "Trial", now, now);
            getFacade().create(p2);

            eu.lpinto.universe.persistence.entities.Plan p3 = new eu.lpinto.universe.persistence.entities.Plan(null, null, "Premium", now, now);
            getFacade().create(p3);
        }

        return super.doFind(userID, options);
    }

    @Override
    public PlanFacade getFacade() {
        return facade;
    }
}
