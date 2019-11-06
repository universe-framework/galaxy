package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "EmailVerification")
public class EmailValidation extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String code;
    private String baseUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = true, nullable = true)
    private Calendar doneDate;

    /*
     * Contructors
     */
    public EmailValidation() {
    }

    public EmailValidation(Long id) {
        super(id);
    }

    public EmailValidation(String email, String baseUrl, String name) {
        super(name);
        this.email = email;
        this.baseUrl = baseUrl;
    }

    public EmailValidation(final String email, final String name, final String baseUrl,
                           final User creator, final Calendar created, final User updater, final Calendar updated, final Long id) {
        super(name, creator, created, updater, updated, id);
        this.email = email;
        this.baseUrl = baseUrl;
        this.code = null;
        setCode();
    }

    public EmailValidation(final String email, final String code, final String baseUrl, final Calendar doneDate,
                           final Long id, final String name, final User creator, final Calendar created,
                           final User updater, final Calendar updated, final Calendar deleted) {
        super(id, name, creator, created, updater, updated, deleted);
        this.email = email;
        this.code = code;
        this.baseUrl = baseUrl;
        this.doneDate = doneDate;
    }

    /*
     * Getters/Setters
     */
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
        this.code = System.currentTimeMillis() + String.format("%05d", getId());
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrl() {
        return baseUrl + code;
    }

    public Calendar getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(final Calendar doneDate) {
        this.doneDate = doneDate;
    }
}
