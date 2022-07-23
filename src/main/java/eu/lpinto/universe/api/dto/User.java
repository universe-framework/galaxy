package eu.lpinto.universe.api.dto;

import java.io.Serializable;

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
    private String preferences;
    private String baseUrl;
    private Boolean god;

    /*
     * Constructors
     */
    public User() {
        super();
    }

    public User(final Long id) {
        super(id);
    }

    public User(String currentAvatar, String email, String password, String preferences, Boolean god) {
        this.currentAvatar = currentAvatar;
        this.email = (email == null ? null : email.isEmpty() ? null : email.trim().toLowerCase());
        this.password = password;
        this.preferences = preferences;
        this.god = god;
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
        this.email = (email == null ? null : email.isEmpty() ? null : email.trim().toLowerCase());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Boolean getGod() {
        return god;
    }

    public void setGod(Boolean god) {
        this.god = god;
    }
}
