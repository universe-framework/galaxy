package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Encapsulates an organization.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Organization extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    private String phone;
    private String facebook;
    private String email;
    private String businessHours;

    private Long avatar;
    private Long plan;
    private Long parent;
    private List<Long> children;
    private List<Long> avatars;

    /*
     * Constructors
     */
    public Organization() {
    }

    public Organization(Long id) {
        super(id);
    }

    public Organization(String phone, String facebook, String email, String businessHours, Long avatar, Long plan, Long parent, List<Long> children, List<Long> avatars, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.phone = phone;
        this.facebook = facebook;
        this.email = email;
        this.businessHours = businessHours;
        this.avatar = avatar;
        this.plan = plan;
        this.parent = parent;
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

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(final String businessHours) {
        this.businessHours = businessHours;
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
