package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * User DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class User extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    private String email;
    private String password;
    private List<String> tokens;

    /*
     * Constructors
     */
    public User() {
        super();
    }

    public User(final Long id) {
        super(id);
    }

    public User(String email, String password, List<String> tokens, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.email = email;
        this.password = password;
        this.tokens = tokens;
    }

    /*
     * Getters/Setters
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(final List<String> tokens) {
        this.tokens = tokens;
    }
}
