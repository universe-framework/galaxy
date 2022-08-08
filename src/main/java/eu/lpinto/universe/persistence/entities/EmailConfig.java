package eu.lpinto.universe.persistence.entities;

import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.UnsupportedEncodingException;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public class EmailConfig {

    private final String replyToEmail;
    private final String replyToName;

    private final String senderName;
    private final String SMTP_USER;
    private final String SMTP_PASSWORD;
    private final String SMTP_ADDR;
    private final Integer SMTP_PORT;

    private final String SMTP_SSL_TRUST;
    private final String IMAP_SSL_TRUST;
    private final Boolean SMTP_TLS;

    /*
     * Constructor
     */
    public EmailConfig() {
        this(UniverseFundamentals.REPLY_TO);
    }

    public EmailConfig(final String replyToEMail) {
        this.replyToName = null;
        this.replyToEmail = replyToEMail != null ? replyToEMail : UniverseFundamentals.REPLY_TO;

        this.senderName = UniverseFundamentals.SENDER_NAME;
        this.SMTP_USER = UniverseFundamentals.SENDER_ADDR;
        this.SMTP_PASSWORD = UniverseFundamentals.SENDER_PASS;
        this.SMTP_ADDR = UniverseFundamentals.SMTP_ADDR;
        this.SMTP_PORT = UniverseFundamentals.SMTP_PORT;

        this.SMTP_SSL_TRUST = "*";
        this.IMAP_SSL_TRUST = "*";
        this.SMTP_TLS = true;
    }

    public EmailConfig(String replyToEmail, String replyToName, String senderName, String SMTP_USER, String SMTP_PASSWORD, String SMTP_ADDR, Integer SMTP_PORT) {
        this.replyToName = replyToName;
        this.replyToEmail = replyToEmail;

        this.senderName = senderName;
        this.SMTP_USER = SMTP_USER;
        this.SMTP_PASSWORD = SMTP_PASSWORD;
        this.SMTP_ADDR = SMTP_ADDR;
        this.SMTP_PORT = SMTP_PORT;

        this.SMTP_SSL_TRUST = "*";
        this.IMAP_SSL_TRUST = "*";
        this.SMTP_TLS = true;
    }

    /*
     * Getters
     */
    public String getReplyToName() {
        return replyToName;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSMTP_USER() {
        return SMTP_USER;
    }

    public String getSMTP_PASSWORD() {
        return SMTP_PASSWORD;
    }

    public String getSMTP_ADDR() {
        return SMTP_ADDR;
    }

    public Integer getSMTP_PORT() {
        return SMTP_PORT;
    }

    public String getSMTP_SSL_TRUST() {
        return SMTP_SSL_TRUST;
    }

    public String getIMAP_SSL_TRUST() {
        return IMAP_SSL_TRUST;
    }

    public Boolean getSMTP_TLS() {
        return SMTP_TLS;
    }

    public Address getFrom() {
        try {
            return senderName != null
                   ? new InternetAddress(SMTP_USER, senderName)
                   : new InternetAddress(SMTP_USER);
        } catch (UnsupportedEncodingException | AddressException ex) {
            return null;
        }
    }

    public Address[] getReplyTo() {
        Address[] result = new Address[1];

        try {
            result[0] = replyToName != null
                        ? new InternetAddress(replyToEmail, replyToName)
                        : new InternetAddress(replyToEmail);
            return result;

        } catch (UnsupportedEncodingException | AddressException ex) {
            return null;
        }
    }
}
