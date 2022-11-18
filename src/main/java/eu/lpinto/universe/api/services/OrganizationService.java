package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.OrganizationDTO;
import eu.lpinto.universe.api.dts.OrganizationDTS;
import eu.lpinto.universe.controllers.OrganizationController;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Organization;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * REST CRUD service for Clinic.
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
@Path("organizations")
public class OrganizationService extends AbstractServiceCRUD<Organization, OrganizationDTO, OrganizationController, OrganizationDTS> {

    @Override
    public Response doFind(Map<String, Object> options) throws PreConditionException, PermissionDeniedException {
        System.out.println(options.get("remoteAddr"));
        return super.doFind(options); //To change body of generated methods, choose Tools | Templates.
    }

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
