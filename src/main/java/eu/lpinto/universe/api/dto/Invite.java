package eu.lpinto.universe.api.dto;

import eu.lpinto.universe.api.dto.AbstractDTO;
import java.util.Calendar;

/**
 * Represents a new employee invited to join a organization.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Invite extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    private Long company;
    private Long organization;
    private String email;
    private String baseUrl;
    private Integer role;

    /*
     * Constructors
     */
    public Invite() {
    }

    public Invite(Long id) {
        super(id);
    }

    public Invite(final Long company, final Long organization, final String email, final Integer role, final String name, final Long creator, final Calendar created, final Long updater, final Calendar updated, final Long id) {
        super(name, creator, created, updater, updated, id);
        this.company = company;
        this.organization = organization;
        this.email = email;
        this.role = role;
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

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
