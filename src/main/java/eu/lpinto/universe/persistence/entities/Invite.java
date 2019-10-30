package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "Invite")
@NamedQueries({
    @NamedQuery(name = "Invite.retrieveByEmail", query = "SELECT i FROM Invite i WHERE i.email = :email")
})
public class Invite extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    private String email;
    private String code;
    private String baseUrl;
    @Column(name = "job")
    private WorkerProfile role;

    /*
     * Contructors
     */
    public Invite() {
    }

    public Invite(Long id) {
        super(id);
    }

    public Invite(Organization organization, String email, String baseUrl, String name) {
        super(name);
        this.organization = organization;
        this.email = email;
        this.baseUrl = baseUrl;
    }

    public Invite(final Organization organization, final String email, final String name, final String baseUrl, final WorkerProfile role,
                  final User creator, final Calendar created, final User updater, final Calendar updated, final Long id) {
        super(name, creator, created, updater, updated, id);
        this.organization = organization;
        this.email = email;
        this.baseUrl = baseUrl;
        this.code = null;
        this.role = role;
        setCode();
    }

    /*
     * Getters/Setters
     */
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCode() {
        this.code = String.format("%04d", organization.getId()) + System.currentTimeMillis() + String.format("%05d", getId());
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public WorkerProfile getRole() {
        return role;
    }

    public void setRole(WorkerProfile role) {
        this.role = role;
    }

    public String getUrl() {
        return baseUrl + code;
    }
}
