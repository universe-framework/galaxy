package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.EmailValidation;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.EmailValidationFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import java.util.GregorianCalendar;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for EmailValidation entity.
 *
 * @author Vítor Martins <vitor.martins@petuniversal.com>
 */
@Stateless
public class EmailValidationController extends AbstractControllerCRUD<EmailValidation> {

    @EJB
    private EmailValidationFacade facade;

    @EJB
    private UserFacade userFacade;

    public EmailValidationController() {
        super(EmailValidation.class.getCanonicalName());
    }

    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, EmailValidation emailValidation) throws PreConditionException, UnknownIdException, PermissionDeniedException {
        String emailValidationdEmail = emailValidation.getEmail();

        if (emailValidation.getName() == null) {
            emailValidation.setName(emailValidation.getEmail(), emailValidation.getName());
        }

        /*
         * Preconditions
         */
        EmailValidation retrieveByEmail = facade.retrieveByEmail(emailValidationdEmail);
        if (retrieveByEmail != null) {
            facade.remove(retrieveByEmail);
        }

        /*
         * Body
         */
        super.doCreate(userID, options, emailValidation);
        emailValidation.setCode();
        super.doUpdate(userID, options, emailValidation);
    }

    @Override
    public void doUpdate(final Long userID, final Map<String, Object> options, final EmailValidation entity) throws PreConditionException {
        if (entity.getDoneDate() == null) { // first time updating
            User savedUser = userFacade.findByEmail(entity.getEmail());
            savedUser.setEmailValidated(Boolean.TRUE);
            userFacade.edit(savedUser);

            entity.setDoneDate(new GregorianCalendar());
            entity.setUpdater(null);
            entity.setCreator(null);
        }

        getFacade().edit(entity);
    }

    @Override
    public EmailValidationFacade getFacade() {
        return facade;
    }
}
