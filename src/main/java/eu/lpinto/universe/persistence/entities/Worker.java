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

    private Boolean manager;

    private Boolean undermanager;

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

    public Worker(Worker other) {
        super(other.getName(), null, null, null, null, null);
        this.professionalLetterNumber = other.professionalLetterNumber;
        this.birthdate = other.birthdate;
        this.organization = other.organization;
        this.employee = other.employee;
        this.enable = other.enable;
        this.externalID = other.externalID;
        this.address = other.address;
        this.phone = other.phone;
        this.mobilePhone = other.mobilePhone;
        this.email = other.email;
        this.role = other.role;
        this.color = other.color;
        this.manager = other.manager;
        this.undermanager = other.undermanager;
    }

    public Worker(Integer professionalLetterNumber, Calendar birthdate, Organization organization, Employee employee,
                  Boolean enable, Long externalID, String address, String phone, String mobilePhone, String email,
                  WorkerProfile role, String color, Boolean manager, Boolean undermanager) {
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
        this.manager = manager;
        this.undermanager = undermanager;
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

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public Boolean getUndermanager() {
        return undermanager;
    }

    public void setUndermanager(Boolean undermanager) {
        this.undermanager = undermanager;
    }
}
