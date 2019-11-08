package eu.lpinto.universe.api.dto;

import java.util.Calendar;

/**
 * Represents a new user email validation.
 *
 * @author Vítor Martins <vitor.martins@petuniversal.com>
 */
public class EmailValidationDTO extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    private String email;
    private String code;
    private String baseUrl;
    private Calendar doneDate;

    /*
     * Constructors
     */
    public EmailValidationDTO() {
    }

    public EmailValidationDTO(Long id) {
        super(id);
    }

    public EmailValidationDTO(final String email, final String code, final String baseUrl, final Calendar doneDate,
                              final Long id, final String name, final Long creator, final Calendar created,
                              final Long updater, final Calendar updated, final Calendar deleted) {
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

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Calendar getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(final Calendar doneDate) {
        this.doneDate = doneDate;
    }
}
