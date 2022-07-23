package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Image;
import eu.lpinto.universe.api.dts.ImageDTS;
import eu.lpinto.universe.controllers.ImageController;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * REST CRUD service for Image.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("images")
public class ImageService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Image, Image, eu.lpinto.universe.controllers.ImageController, ImageDTS> {

    static final private String IMAGES_FOLDER = UniverseFundamentals.AVATAR_FOLDER;

    @EJB
    private eu.lpinto.universe.controllers.ImageController controller;

    @EJB
    private ImageController imageController;

    public ImageService() {
        super(ImageDTS.T);
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public void uploadFile(@Suspended final AsyncResponse asyncResponse,
                           final @Context UriInfo uriInfo,
                           final @Context HttpHeaders headers,
                           final @HeaderParam(UniverseFundamentals.AUTH_USER_ID) Long userID,
                           final @HeaderParam("U-Entity") String entityName,
                           final @HeaderParam("U-Entity-Id") Long entityID,
                           MultipartFormDataInput input) throws IOException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        // Get file data to save
        List<InputPart> inputParts = uploadForm.get("attachment");

        for (InputPart inputPart : inputParts) {
            try {

                /*
                 * Image name and path
                 */
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String fileName = getFileName(header);

                String path = IMAGES_FOLDER + File.separator + entityName + File.separator + entityID;
                File customDir = new File(path);

                if (!customDir.exists()) {
                    customDir.mkdirs();
                }
                fileName = customDir.getCanonicalPath() + File.separator + fileName;

                /*
                 * Write file
                 */
                OutputStream out = null;
                InputStream inputStream = null;
                try {
                    // convert the uploaded file to inputstream
                    inputStream = inputPart.getBody(InputStream.class, null);
                    int read;
                    final byte[] bytes = new byte[1024];

                    out = new FileOutputStream(new File(fileName));
                    while ((read = inputStream.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }

                } catch (FileNotFoundException fne) {
                    asyncResponse.resume(internalError(fne));

                } finally {
                    if (out != null) {
                        out.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }

                /*
                 * Upload image
                 */
                String imageUrl = imageController.upload(fileName, "");
                if (imageUrl == null) {
                    asyncResponse.resume(unprocessableEntity("Image", "cannot upload"));
                    return;
                }

                try {
                    eu.lpinto.universe.persistence.entities.Image savedImage = createImage(imageUrl, fileName, userID);
                    asyncResponse.resume(savedImage);

                } catch (UnknownIdException | PermissionDeniedException | PreConditionException ex) {
                    throw new RuntimeException(ex);
                }

            } catch (RuntimeException ex) {
                asyncResponse.resume(internalError(ex));
            }
        }
    }

    private eu.lpinto.universe.persistence.entities.Image createImage(String imageUrl, final String name, long userID) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        eu.lpinto.universe.persistence.entities.Image newImage = new eu.lpinto.universe.persistence.entities.Image();
        newImage.setUrl(imageUrl);
        newImage.setName(name);
        imageController.doCreate(userID, new HashMap<>(0), newImage);
        return newImage;
    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {

            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    @Override
    protected eu.lpinto.universe.controllers.ImageController getController() {
        return controller;
    }
}
