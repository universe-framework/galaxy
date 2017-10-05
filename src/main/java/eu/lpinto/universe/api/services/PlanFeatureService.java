package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dts.PlanFeatureDTS;
import eu.lpinto.universe.controllers.PlanFeatureController;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
@Path("planFeatures")
public class PlanFeatureService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.PlanFeature, eu.lpinto.universe.api.dto.PlanFeature, PlanFeatureController, PlanFeatureDTS> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanFeatureService.class);

    @EJB
    private PlanFeatureController controller;

    public PlanFeatureService() {
        super(PlanFeatureDTS.T);
    }

    @Override
    protected PlanFeatureController getController() {
        return controller;
    }
}
