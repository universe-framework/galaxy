package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.Worker;
import eu.lpinto.universe.persistence.entities.WorkerProfile;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Rita
 */
@Stateless
public class OrganizationFacade extends AbstractFacade<Organization> {

    @EJB
    private UserFacade userFacade;

    @PersistenceContext
    private EntityManager em;

    public OrganizationFacade() {
        super(Organization.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
     * DAO
     */
    public List<Organization> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Organization> find(final Map<String, Object> options) {
        if (options.containsKey("company")) {
            return findByCompany((Long) options.get("company"));
        }

        Long userID = (Long) options.get("user");
        return findByUser((Long) options.get("user"));
    }

    public List<Organization> findAllByUser(final Long userID) {
        return findByUser(userID);
    }

    @Override
    public Organization retrieve(Long id) {
        Organization savedOrganization = super.retrieve(id);

        savedOrganization.setStaff(getEntityManager()
                .createQuery("select e from Worker e left join fetch e.organization"
                             + " where e.organization.id = :organizationID AND e.role in :role", Worker.class)
                .setParameter("organizationID", id)
                .setParameter("role", Arrays.asList(WorkerProfile.ADMIN, WorkerProfile.DOCTOR, WorkerProfile.NURSE))
                .getResultList());

//        savedClinic.setClients(getEntityManager()
//                .createQuery("select e from Customer e left join fetch e.clinic"
//                             + " where e.clinic.id = :clinicID AND e.profile in :profiles", Customer.class)
//                .setParameter("clinicID", id)
//                .setParameter("profiles", Arrays.asList(WorkerProfile.UNKNOWN))
//                .getResultList());
//        savedClinic.setPatients(getEntityManager()
//                .createQuery("select e from Patients e left join fetch e.clinic"
//                             + " where e.clinic.id = :clinicID", Patient.class).setParameter("clinicID", id).getResultList());
//        savedClinic.setConsumables(getEntityManager()
//                .createQuery("select e from Consumable e left join fetch e.clinic"
//                             + " where e.clinic.id = :clinicID", Consumable.class).setParameter("clinicID", id).getResultList());
//
//        savedClinic.setConsumableTypes(getEntityManager()
//                .createQuery("select e from ConsumableType e left join fetch e.clinic"
//                             + " where e.clinic.id = :clinicID", ConsumableType.class).setParameter("clinicID", id).getResultList());
        savedOrganization.setFull(true);

        return savedOrganization;
    }

    private List<Organization> findByUser(final Long userID) {
        return getEntityManager()
                .createQuery("select o from Organization o WHERE o.id IN (SELECT w.organization.id from Worker w WHERE w.employee.user.id = :userID)", Organization.class)
                .setParameter("userID", userID)
                .getResultList();
    }

    private List<Organization> findByCompany(Long companyID) {
        return getEntityManager()
                .createQuery("SELECT o FROM Organization o WHERE o.company.id = :companyID AND o.enable=1", Organization.class)
                .setParameter("companyID", companyID)
                .getResultList();
    }

}
