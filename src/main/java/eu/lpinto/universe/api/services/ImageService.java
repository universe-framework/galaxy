package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Image;
import eu.lpinto.universe.api.dts.ImageDTS;
import eu.lpinto.universe.api.services.AbstractServiceCRUD;
import eu.lpinto.universe.api.services.AbstractServiceCRUD;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for Image.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("images")
public class ImageService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Image, Image, eu.lpinto.universe.controllers.ImageController, ImageDTS> {

    @EJB
    private eu.lpinto.universe.controllers.ImageController controller;

    public ImageService() {
        super(ImageDTS.T);
    }

    @Override
    protected eu.lpinto.universe.controllers.ImageController getController() {
        return controller;
    }
}
