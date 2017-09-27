package eu.lpinto.universe.controllers;

import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.facades.OrganizationFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for Organization entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class OrganizationController extends AbstractControllerCRUD<Organization> {

    @EJB
    private OrganizationFacade facade;

    public OrganizationController() {
        super(Organization.class.getCanonicalName());
    }

    @Override
    protected OrganizationFacade getFacade() {
        return facade;
    }
}
