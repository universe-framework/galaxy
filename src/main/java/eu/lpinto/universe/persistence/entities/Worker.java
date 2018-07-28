package eu.lpinto.universe.persistence.entities;

import eu.lpinto.universe.persistence.entities.AbstractEntity;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.User;
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

//    @OneToMany
//    private List<HeaderDocument> headerDocuments;
//
//    @OneToMany
//    private List<CalendarResources> calendarResources;
//
//    @OneToMany
//    private List<Consultation> visitDetailWorker1;
//
//    @OneToMany
//    private List<Consultation> VisitDetailWorker2;
//
//    @OneToMany
//    private List<Appointment> calendarAppointments;
//
//    @OneToMany
//    private List<ClinicalData> clinicalData;
//
//    @OneToMany
//    private List<Patient> animals;
    @Enumerated(EnumType.ORDINAL)
    private WorkerProfile role;

    /*
     * Constructors
     */
    public Worker() {
        super();
    }

    public Worker(Long id) {
        super(id);
    }

    public Worker(Integer professionalLetterNumber, Calendar birthdate, Organization organization, Employee employee,
                  Boolean enable, Long externalID, String address, String phone, String mobilePhone, String email, WorkerProfile role,
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
//
//    public List<Patient> getAnimals() {
//        return animals;
//    }
//
//    public void setAnimals(List<Patient> animals) {
//        this.animals = animals;
//    }
//
//    public List<CalendarResources> getCalendarResources() {
//        return calendarResources;
//    }
//
//    public void setCalendarResources(List<CalendarResources> calendarResources) {
//        this.calendarResources = calendarResources;
//    }
//
//    public List<Appointment> getCalendarAppointments() {
//        return calendarAppointments;
//    }
//
//    public void setCalendarAppointments(List<Appointment> calendarAppointments) {
//        this.calendarAppointments = calendarAppointments;
//    }
//
//    public List<HeaderDocument> getHeaderDocuments() {
//        return headerDocuments;
//    }
//
//    public void setHeaderDocuments(List<HeaderDocument> headerDocuments) {
//        this.headerDocuments = headerDocuments;
//    }
//
//    public List<Consultation> getVisitDetailWorker1() {
//        return visitDetailWorker1;
//    }
//
//    public void setVisitDetailWorker1(List<Consultation> visitDetailWorker1) {
//        this.visitDetailWorker1 = visitDetailWorker1;
//    }
//
//    public List<Consultation> getVisitDetailWorker2() {
//        return VisitDetailWorker2;
//    }
//
//    public void setVisitDetailWorker2(List<Consultation> VisitDetailWorker2) {
//        this.VisitDetailWorker2 = VisitDetailWorker2;
//    }
//
//    public List<ClinicalData> getClinicalData() {
//        return clinicalData;
//    }
//
//    public void setClinicalData(List<ClinicalData> clinicalData) {
//        this.clinicalData = clinicalData;
//    }

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

}
