package eu.lpinto.universe.api.servelets;

import eu.lpinto.universe.controllers.CompanyController;
import eu.lpinto.universe.controllers.ImageController;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Company;
import eu.lpinto.universe.persistence.entities.Image;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.*;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@WebServlet(name = "images", urlPatterns = {"/images"})
@MultipartConfig
public class Images extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Images.class);
    private static final String AVATAR_FOLDER = UniverseFundamentals.AVATAR_FOLDER;
    private static final String IMAGES_URL = UniverseFundamentals.AVATAR_URL_PREFIX;
    private static final String IMAGE_NAME = UniverseFundamentals.AVATAR_DEFAULT_FILE_NAME;
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String BEARER = "Bearer ";

    public static String mkLogoURL(long id) {
        return IMAGES_URL + id + IMAGE_NAME;
    }

    public synchronized static String nextFileName(final String sufix) {
        String name = "img_" + System.currentTimeMillis();
        return sufix == null ? name : name + sufix;
    }

    @EJB
    private ImageController imageController;
    @EJB
    private CompanyController companyController;

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeader(response);
        processRequest(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeader(response);
        super.doOptions(request, response);
    }

    private void setCorsHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @SuppressWarnings("NestedAssignment")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        /*
         * Auth
         *
         * String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
         * LOGGER.debug("bearerToken: " + bearerToken);
         * if (bearerToken == null || bearerToken.isEmpty() || !bearerToken.startsWith(BEARER)) {
         * response.sendError(401, "Invalid Authorization token");
         * return;
         * }
         * String TokenID = bearerToken.substring(BEARER.length());
         * User user = tokenController.validate(TokenID);
         */
        User user = new User(0l);
        long userID = user.getId();

        /*
         * Check in it has the required configuration
         */
        if (AVATAR_FOLDER == null) {
            response.sendError(501, "Not Implemented");
            return;
        }

        // Create path components to save the file
        String destination = request.getParameter("destination");
        final String path = AVATAR_FOLDER + destination;
        File f = new File(path);
        f.mkdirs();

        final Part filePart = request.getPart("file");
        final String name = getFileName(filePart);
        final String fileName = nextFileName(name.substring(name.lastIndexOf(".")).toLowerCase());
        final String fullFileName = path + "/" + fileName;

        LOGGER.info("Will upload an image named '" + name + " to '" + fullFileName + "'.");

        OutputStream out = null;
        InputStream filecontent = null;
        final PrintWriter writer = response.getWriter();

        try {
            out = new FileOutputStream(new File(fullFileName));
            filecontent = filePart.getInputStream();

            int read;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            /*
             * Upload image
             */
            String imageUrl = imageController.upload(fullFileName, destination + "/");
            if (imageUrl == null) {
                response.sendError(400, "Error");
                return;
            }

            String[] destinations = destination.split("/");

            /*
             * Update Company
             */
            long id = Long.valueOf(destinations[destinations.length - 1]);
            String entityName = destinations[destinations.length - 2].toLowerCase();

            try {
                Image savedImage;

                switch (entityName) {
                    case "companies":
                        savedImage = createImage(imageUrl, name, userID);
                        Company savedCompany = companyController.retrieve(userID, new HashMap<>(0), id);
                        savedCompany.addAvatar(savedImage);
                        savedCompany.setSelectedAvatar(savedImage);

                        companyController.update(userID, new HashMap<>(0), savedCompany);
                        break;

                    default:
                        response.sendError(400, "Unknown entity name");
                }
            } catch (UnknownIdException | PermissionDeniedException | PreConditionException ex) {
                throw new RuntimeException(ex);
            }

            writer.print(imageUrl);

            //response.sendRedirect(destination);
        } catch (FileNotFoundException fne) {
            writer.println("You either did not specify a file to upload or are "
                           + "trying to upload a file to a protected or nonexistent location.");
            writer.println("<br/> ERROR: " + fne.getMessage());

            LOGGER.error("Problems during file upload. Error: {}", new Object[]{fne.getMessage()});

        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }
            if (writer != null) {
                writer.close();
            }
        }

    }

    private Image createImage(String imageUrl, final String name, long userID) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        Image newImage = new Image();
        newImage.setUrl(imageUrl);
        newImage.setName(name);
        Image savedImage = imageController.create(userID, new HashMap<>(0), newImage);
        return savedImage;
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.info("Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}
