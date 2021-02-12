package eu.lpinto.universe.controllers;

import eu.lpinto.universe.api.util.Digest;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Token;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.TokenFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for Token entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class TokenController {

    @EJB
    private TokenFacade facade;

    @EJB
    private UserFacade userFacade;

    static final String SALT = "eu.lpinto.star.TokenController";

    /*
     * Custom controller services
     */
    public Token login(final User user) throws PreConditionException {
        String accessToken = Digest.getSHA(user.getId() + "." + String.valueOf(System.currentTimeMillis()), SALT);

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new PreConditionException("user", "Missing email");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new PreConditionException("password", "Missing password");
        }

        User savedUser = userFacade.findByEmail(user.getEmail());

        if (savedUser == null) {
            throw new PreConditionException("user", "unknownUser");
        }

        if (!savedUser.getPassword().equals(Digest.getSHA(user.getPassword()))) {
            throw new PreConditionException("password", "invalidPassword");
        }
        /*
        if (user.getEmailValidated() == null || user.getEmailValidated() == false) {
            throw new PreConditionException("user.email", "notValidated");
        }
         */
        Token newToken = new Token(accessToken, savedUser);
        facade.create(newToken);

        return newToken;
    }

    public void logout(final Long userID, final String token) throws PreConditionException {
        Token session;

        session = facade.findByToken(token);

        facade.remove(session);
    }

    public Long validate(final String token) {
        return facade.getUserID(token);
    }

    public TokenFacade getFacade() {
        return facade;
    }
}
