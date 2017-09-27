package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Organization;
import eu.lpinto.universe.api.dts.OrganizationDTS;
import eu.lpinto.universe.controllers.OrganizationController;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for Clinic.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("organizations")
public class OrganizationService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Organization, Organization, OrganizationController, OrganizationDTS> {

    @EJB
    private OrganizationController controller;

    public OrganizationService() {
        super(OrganizationDTS.T);
    }

    @Override
    protected OrganizationController getController() {
        return controller;
    }
}
