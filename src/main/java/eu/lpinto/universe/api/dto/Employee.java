package eu.lpinto.universe.api.dto;

import java.util.Calendar;

/**
 * Represents the relation between and company and a person.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Employee extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    private Long externalID;
    private Long company;
    private Integer profile;
    private Long user;

    /*
     * Constructors
     */
    public Employee() {
    }

    public Employee(Long id) {
        super(id);
    }

    public Employee(Long externalID, Long company, Integer profile, String name, Long id) {
        super(name, id);
        this.externalID = externalID;
        this.company = company;
        this.profile = profile;
    }

    public Employee(Long externalID, Long company, Integer profile, Long user, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.externalID = externalID;
        this.company = company;
        this.profile = profile;
        this.user = user;
    }

    /*
     * Getters/Setters
     */
    public Long getExternalID() {
        return externalID;
    }

    public void setExternalID(Long externalID) {
        this.externalID = externalID;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
