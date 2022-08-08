package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.Email;
import eu.lpinto.universe.persistence.entities.EmailConfig;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public class EmailPlugin {

    private String SMTP_USER;
    private String SMTP_PASSWORD;
    private String SMTP_SSL_TRUST = "*";
    private String IMAP_SSL_TRUST = "*";
    private Boolean SMTP_TLS = true;
    private String SMTP_ADDR;
    private Integer SMTP_PORT;

    private Address from;
    private Address[] replyTo;

    private final Session session;

    public EmailPlugin(EmailConfig config) {
        this.SMTP_USER = config.getSMTP_USER();
        this.SMTP_PASSWORD = config.getSMTP_PASSWORD();
        this.SMTP_ADDR = config.getSMTP_ADDR();
        this.SMTP_PORT = config.getSMTP_PORT();
        this.SMTP_SSL_TRUST = "*";
        this.IMAP_SSL_TRUST = "*";
        this.SMTP_TLS = true;

        this.from = config.getFrom();
        this.replyTo = config.getReplyTo();

        Properties sessionProperties = new Properties();
        sessionProperties.put("mail.smtp.ssl.trust", SMTP_SSL_TRUST);
        sessionProperties.put("mail.imaps.ssl.trust", IMAP_SSL_TRUST);
        sessionProperties.put("mail.smtp.starttls.enable", SMTP_TLS);
        sessionProperties.put("mail.smtp.host", SMTP_ADDR);
        sessionProperties.put("mail.smtp.port", SMTP_PORT);
        sessionProperties.put("mail.smtp.auth", "true");

        session = Session.getInstance(sessionProperties, new javax.mail.Authenticator() {
                                  @Override
                                  protected PasswordAuthentication getPasswordAuthentication() {
                                      String userName = SMTP_USER.contains(">")
                                                        ? SMTP_USER.split(">")[1] : SMTP_USER;
                                      return new PasswordAuthentication(userName, SMTP_PASSWORD);
                                  }
                              });
    }

    public void send(final Email email) throws PreConditionException {
        if (session == null) {
            throw new UnsupportedOperationException("There is no email session available");
        }

        if (SMTP_USER == null) {
            throw new PreConditionException("sender email", "cannot be missing");
        }

        if (SMTP_ADDR == null) {
            throw new PreConditionException("smtp address", "cannot be missing");
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(from);
            message.setReplyTo(replyTo);
            message.setRecipients(Message.RecipientType.TO, email.getRecipients());
            message.setSubject(email.getSubject());

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(email.getBody() == null ? "" : email.getBody(), "text/html; charset=UTF-8");

            // creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // adds attachments
            if (email.getAttachFiles() != null && email.getAttachFiles().length > 0) {
                for (String filePath : email.getAttachFiles()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(filePath);
                    multipart.addBodyPart(attachPart);
                }
            }

            // sets the multi-part as e-mail's content
            message.setContent(multipart);

            /*
             * SEND
             */
            Transport.send(message);

        } catch (IOException | MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

}
