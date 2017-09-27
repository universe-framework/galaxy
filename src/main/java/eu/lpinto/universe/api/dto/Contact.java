package eu.lpinto.universe.api.dto;

import java.io.Serializable;

/**
 * Contact DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Contact implements Serializable {

    public static final long serialVersionUID = 1L;

    private String type;
    private String username;
    private String password;
    private String to;
    private String subject;
    private String message;

    /*
     * Constructors
     */
    public Contact() {
        super();
    }

    public Contact(final String type, final String username, final String password, final String to, final String subject, final String message) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    /*
     * Getters/Setters
     */
    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getTo() {
        return to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
