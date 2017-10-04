package eu.lpinto.universe.controllers;

import eu.lpinto.universe.persistence.entities.Plan;
import eu.lpinto.universe.persistence.facades.PlanFacade;
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
    public PlanFacade getFacade() {
        return facade;
    }
}
