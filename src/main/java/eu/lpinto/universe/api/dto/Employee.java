package eu.lpinto.universe.api.dto;

import java.util.Calendar;

/**
 * Represents the relation between and organization and a person.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Employee extends Person {

    private static final long serialVersionUID = 1L;

    private Long externalID;
    private Long organization;
    private Integer profile;

    /*
     * Constructors
     */
    public Employee() {
    }

    public Employee(Long id) {
        super(id);
    }

    public Employee(Long externalID, Long organization, Integer profile, String name, Long id) {
        super(name, id);
        this.externalID = externalID;
        this.organization = organization;
        this.profile = profile;
    }

    public Employee(Long externalID, Long organization, Integer profile, String email, String phone, String mobilePhone, String nif, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(email, phone, mobilePhone, nif, name, creator, created, updater, updated, id);
        this.externalID = externalID;
        this.organization = organization;
        this.profile = profile;
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

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }
}
