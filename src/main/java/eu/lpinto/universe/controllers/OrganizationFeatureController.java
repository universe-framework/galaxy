package eu.lpinto.universe.controllers;

import eu.lpinto.universe.persistence.entities.OrganizationFeature;
import eu.lpinto.universe.persistence.facades.OrganizationFeatureFacade;
import eu.lpinto.universe.controllers.AbstractControllerCRUD;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Feature;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.facades.Facade;
import eu.lpinto.universe.persistence.facades.FeatureFacade;
import eu.lpinto.universe.persistence.facades.OrganizationFacade;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
@Stateless
public class OrganizationFeatureController extends AbstractControllerCRUD<OrganizationFeature> {

    @EJB
    private OrganizationFeatureFacade facade;

    @EJB
    private OrganizationFacade organizationFacade;

    @EJB
    private FeatureFacade featureFacade;

    public OrganizationFeatureController() {
        super(OrganizationFeature.class.getCanonicalName());
    }

    @Override
    public void doCreate(Long userID, Map<String, Object> options, OrganizationFeature entity) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        Organization savedOrganization = organizationFacade.retrieve(entity.getOrganization().getId());
        Feature savedFeature = featureFacade.retrieve(entity.getFeature().getId());

        entity.setName(savedOrganization.getName(), savedFeature.getName());
        super.doCreate(userID, options, entity);
    }

    @Override
    protected Facade<OrganizationFeature> getFacade() {
        return facade;
    }
}
