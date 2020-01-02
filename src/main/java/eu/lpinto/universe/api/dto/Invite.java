package eu.lpinto.universe.api.dto;

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

    public Invite(final Long company, final Long organization, final String email, final Integer role) {
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
