package eu.lpinto.universe.persistence.entities;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public class Email {

    private final String body;
    private final String recipientEmail;
    private final String subject;
    private final String[] attachFiles;

    public Email(String recipientEmail, String subject, String body) {
        this(recipientEmail, subject, body, null);
    }

    public Email(String recipientEmail, String subject, String body, String... attachFiles) {
        this.body = body;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.attachFiles = attachFiles;
    }

    public String getBody() {
        return body;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public Address[] getRecipients() {
        Address[] result = new Address[1];

        try {
            result[0] = new InternetAddress(recipientEmail);
            return result;

        } catch (AddressException ex) {
            return result;
        }
    }

    public String[] getAttachFiles() {
        return attachFiles;
    }
}
