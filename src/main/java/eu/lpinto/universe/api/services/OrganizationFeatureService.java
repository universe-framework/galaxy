package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dts.OrganizationFeatureDTS;
import eu.lpinto.universe.api.dto.OrganizationFeatureDTO;
import eu.lpinto.universe.controllers.OrganizationFeatureController;
import eu.lpinto.universe.persistence.entities.OrganizationFeature;
import eu.lpinto.universe.api.services.AbstractServiceCRUD;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
@Path("organizationFeatures")
public class OrganizationFeatureService extends AbstractServiceCRUD<OrganizationFeature, OrganizationFeatureDTO, OrganizationFeatureController, OrganizationFeatureDTS> {

    @EJB
    private OrganizationFeatureController controller;

    public OrganizationFeatureService() {
        super(OrganizationFeatureDTS.T);
    }

    @Override
    protected OrganizationFeatureController getController() {
        return controller;
    }
}
