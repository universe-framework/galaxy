package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
public class Invite extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    private String email;
    private String code;
    private String baseUrl;

    /*
     * Contructors
     */
    public Invite() {
    }

    public Invite(Long id) {
        super(id);
    }

    public Invite(Company company, String email, String name, String baseUrl, User creator, Calendar created, User updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.company = company;
        this.email = email;
        this.baseUrl = baseUrl;
        this.code = null;
    }

    /*
     * Getters/Setters
     */
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
