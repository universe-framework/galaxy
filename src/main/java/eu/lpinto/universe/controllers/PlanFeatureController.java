package eu.lpinto.universe.controllers;

import eu.lpinto.universe.persistence.entities.PlanFeature;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
import eu.lpinto.universe.persistence.facades.PlanFeatureFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class PlanFeatureController extends AbstractControllerCRUD<PlanFeature> {

    @EJB
    private PlanFeatureFacade facade;

    public PlanFeatureController() {
        super(PlanFeature.class.getCanonicalName());
    }

    @Override
    public AbstractFacade<PlanFeature> getFacade() {
        return facade;
    }
}
