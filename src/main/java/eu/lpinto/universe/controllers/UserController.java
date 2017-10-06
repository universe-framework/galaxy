package eu.lpinto.universe.controllers;

import eu.lpinto.universe.api.util.Digest;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.PersonFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import java.util.Calendar;
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
    private PersonFacade personFacade;
    @EJB
    private EmailController emailController;

    public UserController() {
        super(User.class.getCanonicalName());
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

    public void recoverPassword(final String email) throws PreConditionException {
        try {
            User user = retrieveByEmailINTERNAL(email);
            String newPassword = "" + Calendar.getInstance().getTimeInMillis();
            user.setPassword(Digest.getSHA(newPassword));
            facade.edit(user);

            String emailMessage = "<p>Boa tarde,</p>"
                                  + "<p>Procedemos à alocação de uma nova password para a sua conta.</p>"
                                  + "<p>Proceda à alteração da password após o primeiro login com os seguintes dados:</p>"
                                  + "<p>email: " + email + "</p>"
                                  + "<p>password: " + newPassword + "</p>"
                                  + "<p>Para alterar a password deve aceder através do menu superior (canto direito) a:</p>"
                                  + "<p>Nome > Conta > Segurança</p>"
                                  + "<p>Qualquer questão não hesite em contactar-nos.</p>"
                                  + "<p>Proceda à alteração da password após o primeiro login</p>"
                                  + "<p>Cumprimentos,</p>"
                                  + "</p>"
                                  + "<p>"
                                  + "A equipa Pet universal"
                                  + "</p>"
                                  + "<p>"
                                  + "<a href=\"http://petuniversal.com/\" target=\"_blank\" style=\"color:#8d8d8d;text-decoration: none;\">www.petuniversal.com</a>"
                                  + "</p>"
                                  + "<p>"
                                  + "    <a href='https://www.facebook.com/petuniversal' target='_blank'>"
                                  + "        <img moz-do-not-send=\"true\" style='border-radius:0;moz-border-radius:0;khtml-border-radius:0;o-border-radius:0;webkit-border-radius:0;ms-border-radius:0;border: 0;width:16px; height:16px;' width='16' height='16' src='https://s3.amazonaws.com/images.wisestamp.com/icons_32/facebook.png'/>"
                                  + "    </a>"
                                  + "    &nbsp;"
                                  + "    <a href='https://www.linkedin.com/company/pet-universal' target='_blank'>"
                                  + "        <img moz-do-not-send=\"true\" style='border-radius:0;moz-border-radius:0;khtml-border-radius:0;o-border-radius:0;webkit-border-radius:0;ms-border-radius:0;border: 0;width:16px; height:16px;' width='16' height='16' src='https://s3.amazonaws.com/images.wisestamp.com/icons_32/linkedin.png'/>"
                                  + "    </a>"
                                  + "    &nbsp;"
                                  + "    <a href='http://twitter.com/Pet_universal' target='_blank'>"
                                  + "        <img moz-do-not-send=\"true\" style='border-radius:0;moz-border-radius:0;khtml-border-radius:0;o-border-radius:0;webkit-border-radius:0;ms-border-radius:0;border: 0;width:16px; height:16px;' width='16' height='16' src='https://s3.amazonaws.com/images.wisestamp.com/icons_32/twitter.png'/>"
                                  + "    </a>"
                                  + "</p>";

            emailController.sendEmail(user.getEmail(), "password", emailMessage);
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
        return userID.equals(entity.getId());
    }

    @Override
    public Boolean assertPremissionsUpdateDelete(Long userID, User entity) throws PermissionDeniedException {
        return userID.equals(entity.getId());
    }
}
