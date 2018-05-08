package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.Company;
import eu.lpinto.universe.persistence.entities.Invite;
import eu.lpinto.universe.persistence.facades.InviteFacade;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Controller for Invite entity.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class InviteController extends AbstractControllerCRUD<Invite> {

    @EJB
    private InviteFacade facade;

    @EJB
    private EmailController emailController;

    @EJB
    private CompanyController companyController;

    public InviteController() {
        super(Invite.class.getCanonicalName());
    }

    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, Invite entity) throws PreConditionException, UnknownIdException, PermissionDeniedException {
        Company savedCompany = companyController.retrieve(userID, options, entity.getCompany().getId());

        String baseURI = ((String) options.get("appPath"));

        String code = "" + String.format("%04d", savedCompany.getId()) + System.currentTimeMillis() + String.format("%03d", entity.getId());
        entity.setCode(code);
        super.doCreate(userID, options, entity);

        String emailMessage = "<h1>Olá " + entity.getName() + ",</h1>"
                              + "<h2>Damos-lhe as boas-vindas &agrave; comunidade Pet universal.</h2>\n"
                              + "<p>Recebeu um convite para se juntar &agrave; empresa: <b>" + savedCompany.getName() + "</b>.</p>\n"
                              + "<p>Poderá criar uma conta acedendo a <a href=\"" + entity.getBaseUrl() + "i/" + code + " target=\"_blank\">" + entity.getBaseUrl() + "i/" + code + "</a>.</p>\n"
                              + "<h2>Ajuda.</h2>\n"
                              + "<p>Poderá consultar ainda um conjunto de <a href=\"https://petuniversal.com/app/#/faq\" target=\"_blank\">questões frequentes</a> e um  <a href=\"https://petuniversal.com/app/#/manual\" target=\"_blank\">manual de utilizador</a> para esclarecimento de dúvidas sobre a plataforma.</p>\n"
                              + "<p>Agradecemos a confiança demonstrada no nosso produto e ficamos ao dispor para qualquer esclarecimento.</p>\n"
                              + "<p>A equipa Pet universal,</p>\n"
                              + "<p><a href=\"http://petuniversal.com/\" target=\"_blank\" style=\"color:#8d8d8d;text-decoration: none;\">www.petuniversal.com</a></p>\n"
                              + "<p><a href='https://www.facebook.com/petuniversal' target=\"_blank\">\n"
                              + "        <img moz-do-not-send=\"true\" style='border-radius:0;moz-border-radius:0;khtml-border-radius:0;o-border-radius:0;webkit-border-radius:0;ms-border-radius:0;border: 0;width:16px; height:16px;' width='16' height='16' src='https://s3.amazonaws.com/images.wisestamp.com/icons_32/facebook.png'/>\n"
                              + "    </a>\n"
                              + "    &nbsp;\n"
                              + "    <a href='https://www.linkedin.com/company/pet-universal' target=\"_blank\">\n"
                              + "        <img moz-do-not-send=\"true\" style='border-radius:0;moz-border-radius:0;khtml-border-radius:0;o-border-radius:0;webkit-border-radius:0;ms-border-radius:0;border: 0;width:16px; height:16px;' width='16' height='16' src='https://s3.amazonaws.com/images.wisestamp.com/icons_32/linkedin.png'/>\n"
                              + "    </a>\n"
                              + "    &nbsp;\n"
                              + "    <a href='http://twitter.com/Pet_universal' target=\"_blank\">\n"
                              + "        <img moz-do-not-send=\"true\" style='border-radius:0;moz-border-radius:0;khtml-border-radius:0;o-border-radius:0;webkit-border-radius:0;ms-border-radius:0;border: 0;width:16px; height:16px;' width='16' height='16' src='https://s3.amazonaws.com/images.wisestamp.com/icons_32/twitter.png'/>\n"
                              + "    </a>\n"
                              + "</p>";

        entity.setCode(code);
        emailController.sendEmail(entity.getEmail(), "Convite", emailMessage);

        super.doUpdate(userID, options, entity);
    }

    @Override
    public InviteFacade getFacade() {
        return facade;
    }
}
