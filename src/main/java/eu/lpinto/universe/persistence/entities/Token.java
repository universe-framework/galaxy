package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
public class Token implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Calendar created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Calendar updated;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    /*
     * Contructors
     */
    public Token() {
    }

    public Token(final String token) {
        this.token = token;
    }

    public Token(final String token, final User user) {
        this.token = token;
        this.user = user;
    }

    public Token(final String token, final Calendar created, final Calendar updated, final User user) {
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

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
