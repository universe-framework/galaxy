package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Image;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Image Facade
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class ImageFacade extends AbstractFacade<Image> {

    @PersistenceContext
    private EntityManager em;

    public ImageFacade() {
        super(Image.class);
    }

    @Override
    public List<Image> find(Map<String, Object> options) throws PreConditionException {
        throw new AssertionError("Cannot list all " + getEntityClass().getSimpleName() + ". Please report this!");
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
