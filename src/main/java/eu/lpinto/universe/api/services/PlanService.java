package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Plan;
import eu.lpinto.universe.api.dts.PlanDTS;
import eu.lpinto.universe.api.services.AbstractServiceCRUD;
import eu.lpinto.universe.api.services.AbstractServiceCRUD;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for Plan.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("plans")
public class PlanService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Plan, Plan, eu.lpinto.universe.controllers.PlanController, PlanDTS> {

    @EJB
    private eu.lpinto.universe.controllers.PlanController controller;

    public PlanService() {
        super(PlanDTS.T);
    }

    @Override
    protected eu.lpinto.universe.controllers.PlanController getController() {
        return controller;
    }
}
