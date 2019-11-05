package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.EmailValidationDTO;
import eu.lpinto.universe.api.dts.EmailValidationDTS;
import eu.lpinto.universe.controllers.EmailValidationController;
import eu.lpinto.universe.persistence.entities.EmailValidation;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 * REST CRUD service for EmailValidation.
 *
 * @author Vítor Martins <vitor.martins@petuniversal.com>
 */
@Path("emailValidations")
public class EmailValidationService extends AbstractServiceCRUD<EmailValidation, EmailValidationDTO, EmailValidationController, EmailValidationDTS> {

    @EJB
    private EmailValidationController controller;

    public EmailValidationService() {
        super(EmailValidationDTS.T);
    }

    @Override
    protected EmailValidationController getController() {
        return controller;
    }
}
