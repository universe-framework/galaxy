package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Company;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA facade for Company entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class CompanyFacade extends AbstractFacade<Company> {

    @PersistenceContext
    private EntityManager em;

    public CompanyFacade() {
        super(Company.class);
    }

    @Override
    public List<Company> find(Map<String, Object> options) {
        Long userID = (Long) options.get("user");

        if (userID.equals(33L)) {
            return getEntityManager()
                    .createQuery("select c from Company c"
                                 + " WHERE c.deleted is null", Company.class)
                    .getResultList();
        }

        return getEntityManager()
                .createQuery("select c from Company c"
                             + " WHERE c.deleted is null"
                             + " AND c.id IN (SELECT e.company.id from Employee e WHERE e.user.id = :userID)", Company.class)
                .setParameter("userID", userID)
                .getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
