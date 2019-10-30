package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.EmailValidation;
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
public class EmailValidationFacade extends AbstractFacade<EmailValidation> {

    @PersistenceContext
    private EntityManager em;

    /*
     * Constructors
     */
    public EmailValidationFacade() {
        super(EmailValidation.class);
    }

    @Override
    public List<EmailValidation> find(Map<String, Object> options) {
        if (options.containsKey("code")) {
            return getEntityManager()
                    .createQuery("SELECT i FROM EmailValidation i WHERE i.code = :codeID", EmailValidation.class)
                    .setParameter("codeID", "" + options.get("code"))
                    .getResultList();

        } else {
            return new ArrayList<>(0);
        }
    }

    public EmailValidation retrieveByEmail(final String email) {
        try {
            return (EmailValidation) em.createNamedQuery("EmailValidation.retrieveByEmail").setParameter("email", email).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public EmailValidation retrieve(Long id) {
        return getEntityManager()
                .createQuery("SELECT i FROM EmailValidation i WHERE i.code = :codeID", EmailValidation.class)
                .setParameter("codeID", id.toString())
                .getSingleResult();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
