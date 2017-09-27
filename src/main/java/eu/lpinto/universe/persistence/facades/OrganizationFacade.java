package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA facade for Organization entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class OrganizationFacade extends AbstractFacade<Organization> {

    @PersistenceContext
    private EntityManager em;

    public OrganizationFacade() {
        super(Organization.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
