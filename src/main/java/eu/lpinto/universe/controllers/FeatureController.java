package eu.lpinto.universe.controllers;

import eu.lpinto.universe.persistence.entities.Feature;
import eu.lpinto.universe.persistence.facades.FeatureFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class FeatureController extends AbstractControllerCRUD<Feature> {

    @EJB
    private FeatureFacade facade;

    public FeatureController() {
        super(Feature.class.getCanonicalName());
    }

    @Override
    public FeatureFacade getFacade() {
        return facade;
    }
}
