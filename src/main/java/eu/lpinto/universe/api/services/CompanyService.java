package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Company;
import eu.lpinto.universe.api.dts.CompanyDTS;
import eu.lpinto.universe.controllers.CompanyController;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for Clinic.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("companies")
public class CompanyService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Company, Company, CompanyController, CompanyDTS> {

    @EJB
    private CompanyController controller;

    public CompanyService() {
        super(CompanyDTS.T);
    }

    @Override
    protected CompanyController getController() {
        return controller;
    }
}
