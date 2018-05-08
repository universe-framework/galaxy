package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Company;
import eu.lpinto.universe.persistence.entities.Image;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
import eu.lpinto.universe.persistence.facades.ImageFacade;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.File;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Controller for Azure storage - for images
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class ImageController extends AbstractControllerCRUD<Image> {

    private static final String IMAGES_LOCAL_FOLDER = UniverseFundamentals.AVATAR_FOLDER;
    private static final String IMAGE_URL_PREFIX = UniverseFundamentals.AVATAR_URL_PREFIX;
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    @EJB
    private ImageFacade facade;

    public ImageController() {
        super("Image");
    }

    /*
     * Upload
     */
    public String upload(final String filePath, final String folder) {
        if (UniverseFundamentals.ENV == null) {
            return Company.DEFAULT_IMG;
        }

        String[] subpath = filePath.split("/");
        return IMAGE_URL_PREFIX + "/" + subpath[subpath.length - 3] + "/" + subpath[subpath.length - 2] + "/" + subpath[subpath.length - 1];
    }

    /*
     * Delete
     */
    @Override
    public void doDelete(final Long userID, final Map<String, Object> options, final Image savedEntity) throws PreConditionException {
        String url = savedEntity.getUrl();

        String[] subsUrls = url.split("/");
        int length = subsUrls.length;
        String fileName = subsUrls[length - 1];
        String entityID = subsUrls[length - 2];
        String entityType = subsUrls[length - 3];

        delete(entityType + "/" + entityID + "/" + fileName);

        super.doDelete(userID, options, savedEntity);
    }

    public void delete(final String filePath) {
        File f = new File(IMAGES_LOCAL_FOLDER + filePath);
        f.delete();
    }

    @Override
    public AbstractFacade<Image> getFacade() {
        return facade;
    }
}
