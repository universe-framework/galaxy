package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.OrganizationDTO;
import eu.lpinto.universe.api.dts.OrganizationDTS;
import eu.lpinto.universe.controllers.OrganizationController;
import eu.lpinto.universe.persistence.entities.Organization;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for Clinic.
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
@Path("organizations")
public class OrganizationService extends AbstractServiceCRUD<Organization, OrganizationDTO, OrganizationController, OrganizationDTS> {

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
