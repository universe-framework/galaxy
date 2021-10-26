package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Invite;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public List<Invite> find(Map<String, Object> options) {
        if (options.containsKey("code")) {
            return getEntityManager()
                    .createQuery("SELECT i FROM Invite i WHERE i.code = :codeID", Invite.class)
                    .setParameter("codeID", "" + options.get("code"))
                    .getResultList();

        } else if (options.containsKey("organization")) {
            return getEntityManager()
                    .createQuery("SELECT i FROM Invite i WHERE i.organization.id = :organizationID", Invite.class)
                    .setParameter("organizationID", options.get("organization"))
                    .getResultList();
        }
        
        throw new AssertionError("Cannot list all " + getEntityClass().getSimpleName() + ". Please report this!");
    }

    public Invite retrieveByEmail(final String email) {
        try {
            return (Invite) em.createNamedQuery("Invite.retrieveByEmail").setParameter("email", email).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
