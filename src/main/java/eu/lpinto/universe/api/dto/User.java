package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * User DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class User extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    /*
     * Relations
     */
    private String currentAvatar;
    /*
     * Properties
     */
    private String email;
    private String password;

    /*
     * Constructors
     */
    public User() {
        super();
    }

    public User(final Long id) {
        super(id);
    }

    public User(String currentAvatar, String email, String password,
                String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.currentAvatar = currentAvatar;
        this.email = email;
        this.password = password;
    }

    /*
     * Getters/Setters
     */
    public String getCurrentAvatar() {
        return currentAvatar;
    }

    public void setCurrentAvatar(String currentAvatar) {
        this.currentAvatar = currentAvatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
