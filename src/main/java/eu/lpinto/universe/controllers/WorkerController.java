package eu.lpinto.universe.controllers;

import eu.lpinto.universe.api.util.Digest;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnexpectedException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.AbstractEntity;
import eu.lpinto.universe.persistence.entities.Employee;
import eu.lpinto.universe.persistence.entities.EmployeeProfile;
import eu.lpinto.universe.persistence.entities.Organization;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.entities.Worker;
import eu.lpinto.universe.persistence.entities.WorkerProfile;
import eu.lpinto.universe.persistence.facades.EmployeeFacade;
import eu.lpinto.universe.persistence.facades.Facade;
import eu.lpinto.universe.persistence.facades.OrganizationFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import eu.lpinto.universe.persistence.facades.WorkerFacade;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Rita Portas
 */
@Stateless
public class WorkerController extends AbstractControllerCRUD<Worker> {

    @EJB
    private UserFacade userFacade;

    @EJB
    private EmployeeFacade employeeFacade;

    @EJB
    private WorkerFacade facade;

    @EJB
    private OrganizationFacade organizationFacade;

    public WorkerController() {
        super(Worker.class.getCanonicalName());
    }

    @Override
    public Facade<Worker> getFacade() {
        return facade;
    }

    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, Worker newWorker) throws PreConditionException, UnknownIdException, PermissionDeniedException {

        String password = newWorker.getAddress();
        newWorker.setAddress(null);
        Worker workerAux = newWorker;

        if(newWorker.getOrganization() == null || newWorker.getRole() == null) {
            throw new UnexpectedException("The Worker doesn't have organization or role");
        }

        List<Worker> savedWorkers = facade.findByOrganizationAndEmail(newWorker.getOrganization().getId(), newWorker.getEmail());

        if(savedWorkers == null || savedWorkers.isEmpty()) {

            //Hopi
            if(newWorker.getEnable() == null) {
                newWorker.setEnable(true);   //DEFAULT (Enable = true)
            }

            if(newWorker.getId() != null) {
                newWorker.setExternalID(newWorker.getId());
                newWorker.setId(null);
            }

        } else {
            Worker savedWorker = savedWorkers.get(0);
            if(savedWorker.getEnable() == null || savedWorker.getEnable() == false) {
                savedWorker.setEnable(true);
            }

            workerAux = savedWorker;
        }

        if(workerAux.getEmployee() == null) {
            User user = null;

            if(workerAux.getEmail() != null) {
                user = new User(workerAux.getEmail(), Digest.getSHA(password), workerAux.getName());
                userFacade.create(user);
            }

            Organization savedOrganization = organizationFacade.retrieve(workerAux.getOrganization().getId());

            Employee newEmployee = new Employee(workerAux.getExternalID(),
                                                savedOrganization.getCompany(),
                                                workerAux.getRole() == WorkerProfile.ADMIN ? EmployeeProfile.ADMIN : EmployeeProfile.WORKER,
                                                user,
                                                AbstractEntity.buildCode(savedOrganization.getName(), workerAux.getName()));

            employeeFacade.create(newEmployee);
            newWorker.setEmployee(newEmployee);
        }

        super.doCreate(userID, options, newWorker);

        if(UniverseFundamentals.IMPORTS_FOLDER != null) {

            String baseDir = UniverseFundamentals.IMPORTS_FOLDER;

            Organization savedOrganization = organizationFacade.retrieve(newWorker.getOrganization().getId());

            File src = new File(baseDir + File.separator + "companies" + File.separator
                                + savedOrganization.getCompany().getId() + File.separator + "employees.xls");

            if(src.exists()) {
                try(FileInputStream inputFile = new FileInputStream(src)) {
                    HSSFWorkbook workbook = new HSSFWorkbook(inputFile);
                    HSSFSheet sheet = workbook.getSheetAt(0);

                    Iterator<Row> rowIterator = sheet.iterator();
                    rowIterator.next();
                    while(rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        String auxEmail = row.getCell(1).getStringCellValue();
                        Integer role = new Double(row.getCell(2).getNumericCellValue()).intValue();
                        Long organizationID = new Double(row.getCell(3).getNumericCellValue()).longValue();

                        if(newWorker.getEmail().equals(auxEmail) && !newWorker.getOrganization().getId().equals(organizationID)) {
                            Worker w = new Worker(new Organization(organizationID), newWorker.getEmployee(), true, auxEmail, WorkerProfile.values()[role], newWorker.getName());
                            facade.create(w);
                        }
                    }

                } catch(FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch(IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void doUpdate(final Long userID, final Map<String, Object> options, Worker newWorker) throws PreConditionException {
        try {
            Worker savedWorker = super.doRetrieve(userID, options, newWorker.getId());

            //Hopi
            newWorker.setExternalID(savedWorker.getExternalID());
            newWorker.setEnable(true);
            newWorker.setOrganization(savedWorker.getOrganization());
            super.doUpdate(userID, options, newWorker);

        } catch(UnknownIdException ex) {
            throw new PreConditionException("id", "Unknonwn");
        }
    }

    @Override
    public void doDelete(final Long userID, final Map<String, Object> options, Worker savedWorker) throws PreConditionException {
        try {
            Worker newWorker = super.doRetrieve(userID, options, savedWorker.getId());
            savedWorker.setExternalID(newWorker.getExternalID());
            savedWorker.setEnable(false);
            savedWorker.setOrganization(newWorker.getOrganization());
            savedWorker.setRole(newWorker.getRole());
            super.doUpdate(userID, options, savedWorker);

        } catch(UnknownIdException ex) {
            throw new PreConditionException("id", "Unknonwn");
        }
    }

    /*
     * Helpers
     */
    @Override
    public Boolean assertPremissionsCreate(Long userID, Worker entity) throws PermissionDeniedException {
        return true;
    }

    @Override
    public Boolean assertPremissionsRead(Long userID, Worker entity) throws PermissionDeniedException {
        if(userID != null) {
            return true;
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public Boolean assertPremissionsUpdateDelete(Long userID, Worker entity) throws PermissionDeniedException {
        try {
            Worker savedWorker = super.doRetrieve(userID, new HashMap<>(0), entity.getId());
            if(savedWorker.getEnable() == true) {
                return true;
            } else {
                throw new PermissionDeniedException();
            }
        } catch(UnknownIdException | PreConditionException ex) {
            Logger.getLogger(WorkerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
