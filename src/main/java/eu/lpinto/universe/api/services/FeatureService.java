package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Feature;
import eu.lpinto.universe.api.dts.FeatureDTS;
import eu.lpinto.universe.controllers.FeatureController;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for Feature.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("features")
public class FeatureService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Feature, Feature, eu.lpinto.universe.controllers.FeatureController, FeatureDTS> {

    @EJB
    private eu.lpinto.universe.controllers.FeatureController controller;

    public FeatureService() {
        super(FeatureDTS.T);
    }

    @Override
    protected FeatureController getController() {
        return controller;
    }
}
