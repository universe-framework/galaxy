package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Rita Portas
 */
@Entity
@Table(name = "Worker")
public class Worker extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer professionalLetterNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar birthdate;

    @ManyToOne
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    private Employee employee;

    private Boolean enable;

    private Long externalID;

    private String address;
    private String phone;
    private String mobilePhone;
    private String email;

    @Enumerated(EnumType.ORDINAL)
    private WorkerProfile role;
    
    private String color;

    /*
     * Constructors
     */
    public Worker() {
        super();
    }

    public Worker(Long id) {
        super(id);
    }

    public Worker(Organization organization, Employee employee, Boolean enable, String email, WorkerProfile role, String name) {
        super(name);
        this.organization = organization;
        this.employee = employee;
        this.enable = enable;
        this.email = email;
        this.role = role;
    }

    public Worker(Integer professionalLetterNumber, Calendar birthdate, Organization organization, Employee employee,
                  Boolean enable, Long externalID, String address, String phone, String mobilePhone, String email, 
                  WorkerProfile role, String color,
                  String name, User creator, Calendar created, User updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
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
        this.color = color;
    }

    /*
     * Getters/Setters
     */
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public WorkerProfile getRole() {
        return role;
    }

    public void setRole(WorkerProfile role) {
        this.role = role;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
