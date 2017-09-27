package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Image;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
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
    protected EntityManager getEntityManager() {
        return em;
    }
}
