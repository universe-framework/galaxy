package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Invite;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * EJB facade for shelter-people relation.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class InviteFacade extends AbstractFacade<Invite> {

    @PersistenceContext
    private EntityManager em;

    /*
     * Constructors
     */
    public InviteFacade() {
        super(Invite.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
