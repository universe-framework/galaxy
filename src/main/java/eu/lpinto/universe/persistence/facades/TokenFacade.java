package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Token;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class TokenFacade extends AbstractFacade<Token> {

    @PersistenceContext
    private EntityManager em;

    public TokenFacade() {
        super(Token.class);
    }

    /*
     * DAO
     */

    @Override
    public List<Token> find(Map<String, Object> options) throws PreConditionException {
        throw new AssertionError("Cannot list all " + getEntityClass().getSimpleName() + ". Please report this!");
    }
    
    public Token findByToken(final String token) {
        try {
            Token session = getEntityManager().createQuery(
                    "SELECT t FROM Token t WHERE t.token = :token", Token.class)
                    .setParameter("token", token)
                    .getSingleResult();

            return session;
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Long getUserID(final String token) {
        try {
            return getEntityManager().createQuery(
                    "SELECT u.id FROM User u INNER JOIN Token t ON t.user.id = u.id WHERE t.token = :token", Long.class)
                    .setParameter("token", token)
                    .getSingleResult();

        } catch (NoResultException ex) {
            return null;
        }
    }

    /*
     * CRUD
     */
    @Override
    public void create(final Token entity) throws PreConditionException {
        if (entity.getUser() != null && entity.getUser().getTokens() != null && !entity.getUser().getTokens().isEmpty()) {
            Token oldToken = entity.getUser().getTokens().get(0);
            this.remove(oldToken);
        }

        Calendar now = new GregorianCalendar();

        entity.setCreated(now);
        entity.setUpdated(now);

        super.create(entity);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
