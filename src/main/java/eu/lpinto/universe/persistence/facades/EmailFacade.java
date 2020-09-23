package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.IOException;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class EmailFacade {

    private String systemEmail = UniverseFundamentals.SUPPORT_ADDR;
    private String replyTo = UniverseFundamentals.REPLY_TO;
    private String senderName = UniverseFundamentals.SENDER_NAME;
    private String senderEmail = UniverseFundamentals.SENDER_ADDR;
    private String senderPassword = UniverseFundamentals.SENDER_PASS;
    private String SMTP_SSL_TRUST = "*";
    private String IMAP_SSL_TRUST = "*";
    private Boolean SMTP_TLS = true;
    private String SMTP_ADDR = UniverseFundamentals.SMTP_ADDR;
    private Integer SMTP_PORT = UniverseFundamentals.SMTP_PORT;

    private final Session session;

    public EmailFacade() {
        if (SMTP_ADDR == null || SMTP_ADDR == null || SMTP_PORT == null) {
            session = null;

        } else {
            Properties props = new Properties();
            props.put("mail.smtp.ssl.trust", SMTP_SSL_TRUST);
            props.put("mail.imaps.ssl.trust", IMAP_SSL_TRUST);
            props.put("mail.smtp.starttls.enable", SMTP_TLS);
            props.put("mail.smtp.host", SMTP_ADDR);
            props.put("mail.smtp.port", SMTP_PORT);

            if (senderEmail == null) {
                session = Session.getInstance(props);

            } else {
                if (senderEmail != null && senderPassword != null) {
                    props.put("mail.smtp.auth", "true");
                    session = Session.getInstance(props, new javax.mail.Authenticator() {
                                              @Override
                                              protected PasswordAuthentication getPasswordAuthentication() {
                                                  String userName = senderEmail.contains(">")
                                                                    ? senderEmail.split(">")[1] : senderEmail;
                                                  return new PasswordAuthentication(userName, senderPassword);
                                              }
                                          });
                } else {
                    session = null;
                }
            }
        }
    }

    public void sendEmail(final String subject, final String emailMessage) {
        sendEmail(senderEmail, senderName, replyTo, senderName, systemEmail, subject, emailMessage);
    }

    public void sendEmail(final String receiverEmail, final String subject, final String emailMessage) {
        sendEmail(senderEmail, senderName, replyTo, senderName, receiverEmail, subject, emailMessage);
    }

    public void sendEmail(final String receiverEmail, final String subject, final String emailMessage, final String file) {
        sendEmail(senderEmail, senderName, replyTo, senderName, receiverEmail, subject, emailMessage, file);
    }

    public void sendEmail(final String replyTo, final String receiverEmail, final String subject, final String emailMessage, final String file) {
        sendEmail(senderEmail, senderName, replyTo, senderName, receiverEmail, subject, emailMessage, file);
    }

    public void sendEmail(final String from, final String fromName, final String replyTo, final String replyToName,
                          final String receiverEmail, final String subject, final String emailMessage, final String... attachFiles) {
        if (session == null) {
            return;
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(fromName == null ? new InternetAddress(from) : new InternetAddress(from, fromName));
            message.setReplyTo(new Address[]{replyToName == null ? new InternetAddress(replyTo) : new InternetAddress(replyTo, replyToName)});
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
            message.setSubject(subject == null ? senderEmail.split("@")[1] : subject);

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(emailMessage == null ? "" : emailMessage, "text/html; charset=UTF-8");

            // creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // adds attachments
            if (attachFiles != null && attachFiles.length > 0) {
                for (String filePath : attachFiles) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(filePath);
                    multipart.addBodyPart(attachPart);
                }
            }

            // sets the multi-part as e-mail's content
            message.setContent(multipart);

            Transport.send(message);
        } catch (IOException | MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
