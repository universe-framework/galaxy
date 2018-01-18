package eu.lpinto.universe.api.dto;

import java.util.Calendar;

/**
 * Represents a new employee invited to join a company.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Invite extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    private Long company;
    private String email;
    private String baseUrl;

    /*
     * Constructors
     */
    public Invite() {
    }

    public Invite(Long id) {
        super(id);
    }

    public Invite(Long company, String email, String name, String baseUrl, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.company = company;
        this.email = email;
        this.baseUrl = baseUrl;
    }

    /*
     * Getters/Setters
     */
    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
