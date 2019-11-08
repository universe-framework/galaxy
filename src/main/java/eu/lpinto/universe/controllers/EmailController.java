package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.facades.EmailFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * Controller for E-mails.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class EmailController {

    public static String streamToString(final InputStream inputStream) {
        try (
                final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.lines().parallel().collect(Collectors.joining("\n"));

        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private final String passwordRestStrPT;
    private final String passwordRestStrES;
    private final String passwordRestStrEN;
    private final String inviteStrPT;
    private final String inviteStrES;
    private final String inviteStrEN;
    private final String validationStrPT;
    private final String validationStrES;
    private final String validationStrEN;

    @EJB
    private EmailFacade facade;

    @EJB
    private UserFacade userFacade;

    public EmailController() {
        passwordRestStrPT = setDefaultBody("password_recovery", "pt", "A sua nova password é: ${password}");
        passwordRestStrES = setDefaultBody("password_recovery", "es", "La nueva contraseña es: ${password}");
        passwordRestStrEN = setDefaultBody("password_recovery", "en", "Your new password is: ${password}");

        inviteStrPT = setDefaultBody("invite", "pt", "Para aceitar o convite aceda a: ${inviteUrl}");
        inviteStrES = setDefaultBody("invite", "es", "El enlace de invitación es: ${inviteUrl}");
        inviteStrEN = setDefaultBody("invite", "en", "Your invitation link is: ${inviteUrl}");

        validationStrPT = setDefaultBody("validation", "pt", "Para validar o email aceda a: ${url}");
        validationStrES = setDefaultBody("validation", "es", "El enlace de validacion es: ${url}");
        validationStrEN = setDefaultBody("validation", "en", "Your email validation link is: ${url}");
    }

    private String setDefaultBody(final String fileName, final String lang, final String defaultBody) {
        InputStream inputStreamEN = EmailController.class.getClassLoader().getResourceAsStream(fileName + "-" + lang + ".html");
        if (inputStreamEN != null) {
            return streamToString(inputStreamEN);
        } else {
            inputStreamEN = EmailController.class.getClassLoader().getResourceAsStream("default_" + fileName + "-" + lang + ".html");
            if (inputStreamEN != null) {
                return streamToString(inputStreamEN);
            } else {
                return defaultBody;
            }
        }
    }

    public void recoverPassword(final String lang, final String email, String newPassword) throws PreConditionException {
        String content;
        String subject;

        if (lang != null && (lang.contains("pt-PT")
                             || lang.contains("pt-BR")
                             || lang.contains("pt"))) {
            subject = "Recuperar password";
            content = passwordRestStrPT;

        } else if (lang != null && (lang.contains("es-ES")
                                    || lang.contains("es-MX")
                                    || lang.contains("es"))) {
            subject = "Recuperación de contraseña";
            content = passwordRestStrES;

        } else {
            subject = "Password recovery";
            content = passwordRestStrEN;
        }

        content = content.replaceAll("\\$\\{userName\\}", userFacade.findByEmail(email).getName());
        content = content.replaceAll("\\$\\{email\\}", email);
        content = content.replaceAll("\\$\\{password\\}", newPassword);

        facade.sendEmail(email, subject, content);

    }

    public void sendValidation(final String lang, final String destinationEmail, final String userName, final String url) {
        String content;
        String subject;

        if (lang != null && (lang.contains("pt-PT")
                             || lang.contains("pt-BR")
                             || lang.contains("pt"))) {
            subject = "Validar endereço de email";
            content = validationStrPT;

        } else if (lang != null && (lang.contains("es-ES")
                                    || lang.contains("es-MX")
                                    || lang.contains("es"))) {
            subject = "Validar correo electrónico";
            content = validationStrES;

        } else {
            subject = "Validate email";
            content = validationStrEN;
        }

        content = content.replaceAll("\\$\\{userName\\}", userName);
        content = content.replaceAll("\\$\\{url\\}", url);

        facade.sendEmail(destinationEmail, subject, content);
    }

    public void sendInvite(final String lang, final String destinationEmail, final String userName,
                           final String creatorName, final String organizationName, final String inviteUrl) {
        String content;
        String subject;

        if (lang != null && (lang.contains("pt-PT")
                             || lang.contains("pt-BR")
                             || lang.contains("pt"))) {
            subject = "Convite";
            content = inviteStrPT;

        } else if (lang != null && (lang.contains("es-ES")
                                    || lang.contains("es-MX")
                                    || lang.contains("es"))) {
            subject = "Invitación";
            content = inviteStrES;

        } else {
            subject = "Invite";
            content = inviteStrEN;
        }

        content = content.replaceAll("\\$\\{userName\\}", userName);
        content = content.replaceAll("\\$\\{creatorName\\}", creatorName);
        content = content.replaceAll("\\$\\{organizationName\\}", organizationName);
        content = content.replaceAll("\\$\\{inviteUrl\\}", inviteUrl);

        facade.sendEmail(destinationEmail, subject, content);
    }

}
