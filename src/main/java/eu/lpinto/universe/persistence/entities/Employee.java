package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "Employee")
public class Employee extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long externalID;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Company company;

    @Enumerated(EnumType.ORDINAL)
    private EmployeeProfile profile;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private User user;

    /*
     * Constructors
     */
    public Employee() {
        super();
    }

    public Employee(final Long id) {
        super(id);
    }

    public Employee(final Company company, final EmployeeProfile profile, final User user) {
        this.company = company;
        this.profile = profile;
        this.user = user;
    }

    public Employee(final Long externalID, final Company company, final EmployeeProfile profile) {
        super();
        this.externalID = externalID;
        this.company = company;
        this.profile = profile;
    }

    public Employee(final Long externalID, final Company company, final EmployeeProfile profile,
                    final Long id, final String name, final Calendar created, final Calendar updated) {

        this.externalID = externalID;
        this.company = company;
        this.profile = profile;
    }

    public Employee(Long externalID, Company company, EmployeeProfile profile, User user, String name) {
        super(name);
        this.externalID = externalID;
        this.company = company;
        this.profile = profile;
        this.user = user;
    }

    public Employee(Long externalID, Company company, EmployeeProfile profile, User user, String name, User creator, Calendar created, User updater, Calendar updated, Long id) {
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(final Company company) {
        assertNotNull(company);

        this.company = company;
    }

    public EmployeeProfile getProfile() {
        return profile;
    }

    public void setProfile(final EmployeeProfile profile) {
        assertNotNull(profile);

        this.profile = profile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
