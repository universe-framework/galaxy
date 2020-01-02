package eu.lpinto.universe.api.dto;

import java.io.Serializable;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Person extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;
    private String email;
    private String phone;
    private String mobilePhone;
    private String nif;

    /*
     * Constructors
     */
    public Person() {
        super();
    }

    public Person(final Long id) {
        super(id);
    }

    public Person(String name, Long id) {
        super(name, id);
    }

    public Person(String email, String phone, String mobilePhone, String nif) {
        this.email = email;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.nif = nif;
    }

    /*
     * Getters
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }
}
