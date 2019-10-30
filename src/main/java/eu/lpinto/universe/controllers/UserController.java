package eu.lpinto.universe.controllers;

import eu.lpinto.universe.api.util.Digest;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.EmailValidation;
import eu.lpinto.universe.persistence.entities.Image;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.EmailValidationFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for User entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class UserController extends AbstractControllerCRUD<User> {

    @EJB
    private UserFacade facade;

    @EJB
    private EmailController emailController;

    @EJB
    private ImageController imageController;

    @EJB
    private EmailValidationFacade emailValidationFacade;

    public UserController() {
        super(User.class.getCanonicalName());
    }

    /*
     * CRUD
     */
    @Override
    public void doCreate(Long userID, Map<String, Object> options, User entity) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        Image newImage = null;

        if (entity.getCurrentAvatar() != null) {
            newImage = entity.getCurrentAvatar();
            entity.setCurrentAvatar(null);
        }

        super.doCreate(userID, options, entity);

        if (newImage != null) {
            newImage.setName("User_" + entity.getId() + "_Image#1");
            imageController.create(entity.getId(), options, newImage);
            entity.setCurrentAvatar(newImage);
        }

        /*
         * Create invite for worker
         */
        EmailValidation newInvite = new EmailValidation(entity.getEmail(), entity.getBaseUrl(), entity.getName());
        emailValidationFacade.create(newInvite);
        newInvite.setCode();
        emailValidationFacade.edit(newInvite);

        emailController.sendValidation((String) options.get("locale"), entity.getEmail(), entity.getName(), newInvite.getUrl());
        */
    }

    @Override
    public void doUpdate(Long userID, Map<String, Object> options, User entity) throws PreConditionException {
        Image newImage = null;

        if (entity.getCurrentAvatar() != null) {
            try {
                User savedUser = doRetrieve(userID, options, userID);
                if (savedUser.getCurrentAvatar() != null) {
                    Image savedAvatar = savedUser.getCurrentAvatar();
                    if (savedAvatar.getUrl().equals(entity.getCurrentAvatar().getUrl())) {
                        entity.setCurrentAvatar(savedAvatar);

                    } else {
                        newImage = entity.getCurrentAvatar();
                        entity.setCurrentAvatar(null);
                    }
                }

            } catch (UnknownIdException ex) {
                throw new PreConditionException("id", "Unknown");
            }
        }

        super.doUpdate(userID, options, entity);

        if (newImage != null) {
            try {
                newImage.setName("User_" + entity.getId() + "_Image#2");
                imageController.create(entity.getId(), options, newImage);
                entity.setCurrentAvatar(newImage);

            } catch (UnknownIdException ex) {
                throw new PreConditionException("id", "Unknown");

            } catch (PermissionDeniedException ex) {
                throw new PreConditionException("image", "missing create permissions");
            }
        }
    }

    /*
     * Custom controller services
     */
    public User retrieveByEmail(final Long userID, final String email) throws UnknownIdException, PreConditionException {
        /*
         * Preconditions
         */
        if (userID == null) {
            throw missingParameter("userID");
        }

        if (email == null) {
            throw missingParameter("email");
        }

        /*
         * Body
         */
        User savedUser;

        try {
            savedUser = facade.findByEmail(email);

        } catch (RuntimeException ex) {
            throw internalError(ex);
        }

        if (savedUser == null) {
            throw new UnknownIdException(User.class.getCanonicalName(), -1L);
        }

        if (userID.equals(savedUser.getId()) || isSystemAdmin(userID)) {
            /*
             * Own data, or admin - Retrieve all data
             */
            return savedUser;
        } else {
            /*
             * TODO retrieve only public data
             */
            return savedUser;
        }
    }

    public User retrieveByEmailINTERNAL(final String email) throws UnknownIdException, PreConditionException {
        /*
         * Preconditions
         */
        if (email == null) {
            throw missingParameter("email");
        }

        /*
         * Body
         */
        User savedUser;

        try {
            savedUser = facade.findByEmail(email);

        } catch (RuntimeException ex) {
            throw internalError(ex);
        }

        if (savedUser == null) {
            throw new UnknownIdException(User.class.getCanonicalName(), -1L);
        }

        return savedUser;
    }

    public void recoverPassword(final String lang, final String email) throws PreConditionException {
        try {
            User user = retrieveByEmailINTERNAL(email);
            String newPassword = "" + Calendar.getInstance().getTimeInMillis();
            user.setPassword(Digest.getSHA(newPassword));
            facade.edit(user);

            emailController.recoverPassword(lang, email, newPassword);

        } catch (UnknownIdException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public UserFacade getFacade() {
        return facade;
    }

    @Override
    public Boolean assertPremissionsRead(Long userID, User entity) throws PermissionDeniedException {
        return true;
    }

    @Override
    public Boolean assertPremissionsUpdateDelete(Long userID, User entity) throws PermissionDeniedException {
        return userID.equals(entity.getId());
    }
}
