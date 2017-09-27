package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private Calendar created;
    private Calendar updated;
    private Long user;

    /*
     * Constructors
     */
    protected Token() {
    }

    public Token(final String token) {
        this.token = token;
    }

    public Token(final String token, final Calendar created, final Calendar updated, final Long user) {
        this.token = token;
        this.created = created;
        this.updated = updated;
        this.user = user;
    }

    /*
     * Getters/Setters
     */
    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(final Calendar created) {
        this.created = created;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(final Calendar updated) {
        this.updated = updated;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(final Long user) {
        this.user = user;
    }
}
