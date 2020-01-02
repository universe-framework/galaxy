package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Rita
 */
public class WorkerDTO extends AbstractDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer professionalLetterNumber;
    private Calendar birthdate;
    private Long organization;
    private Long employee;
    private Boolean enable;
    private Long externalID;
    private String address;
    private String phone;
    private String mobilePhone;
    private String email;
    private Integer role;

    public WorkerDTO() {
        super();
    }

    public WorkerDTO(Integer professionalLetterNumber, Calendar birthdate,
                     Long organization, Long employee, Boolean enable, Long externalID, String address, String phone,
                     String mobilePhone, String email, Integer role) {
        this.professionalLetterNumber = professionalLetterNumber;
        this.birthdate = birthdate;
        this.organization = organization;
        this.employee = employee;
        this.enable = enable;
        this.externalID = externalID;
        this.address = address;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.role = role;
    }

    public Integer getProfessionalLetterNumber() {
        return professionalLetterNumber;
    }

    public void setProfessionalLetterNumber(Integer professionalLetterNumber) {
        this.professionalLetterNumber = professionalLetterNumber;
    }

    public Calendar getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Calendar birthdate) {
        this.birthdate = birthdate;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long clinic) {
        this.organization = clinic;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Long getEmployee() {
        return employee;
    }

    public void setEmployee(Long employee) {
        this.employee = employee;
    }

    public Long getExternalID() {
        return externalID;
    }

    public void setExternalID(Long externalID) {
        this.externalID = externalID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
