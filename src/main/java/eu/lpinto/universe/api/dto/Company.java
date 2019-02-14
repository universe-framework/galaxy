package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Encapsulates a company.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Company extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long avatar;
    private Long plan;
    private Long parent;

    private String phone;
    private String facebook;
    private String email;
    private String vatNumber;
    private String customField;

    private List<Long> children;
    private List<Long> avatars;

    /*
     * Constructors
     */
    public Company() {
    }

    public Company(Long id) {
        super(id);
    }

    public Company(String phone, String facebook, String email, String vatNumber, Long avatar, Long plan, Long parent, String customField,
                   List<Long> children, List<Long> avatars, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.phone = phone;
        this.facebook = facebook;
        this.email = email;
        this.vatNumber = vatNumber;
        this.avatar = avatar;
        this.plan = plan;
        this.parent = parent;
        this.customField = customField;
        this.children = children;
        this.avatars = avatars;
    }

    /*
     * Getters/Setters
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(final String facebook) {
        this.facebook = facebook;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(final String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Long getAvatar() {
        return avatar;
    }

    public void setAvatar(final Long avatar) {
        this.avatar = avatar;
    }

    public Long getPlan() {
        return plan;
    }

    public void setPlan(final Long plan) {
        this.plan = plan;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(final Long parent) {
        this.parent = parent;
    }

    public String getCustomField() {
        return customField;
    }

    public void setCustomField(String customField) {
        this.customField = customField;
    }

    public List<Long> getChildren() {
        return children;
    }

    public void setChildren(final List<Long> children) {
        this.children = children;
    }

    public List<Long> getAvatars() {
        return avatars;
    }

    public void setAvatars(final List<Long> avatars) {
        this.avatars = avatars;
    }
}
