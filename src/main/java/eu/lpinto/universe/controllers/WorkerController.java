package eu.lpinto.universe.controllers;

import eu.lpinto.universe.persistence.entities.Worker;
import eu.lpinto.universe.persistence.facades.WorkerFacade;
import eu.lpinto.universe.controllers.AbstractControllerCRUD;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnexpectedException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.facades.Facade;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Rita Portas
 */
@Stateless
public class WorkerController extends AbstractControllerCRUD<Worker> {

    @EJB
    private WorkerFacade facade;

    public WorkerController() {
        super(Worker.class.getCanonicalName());
    }

    @Override
    public Facade<Worker> getFacade() {
        return facade;
    }

    @Override
    public void doCreate(final Long userID, final Map<String, Object> options, Worker newWorker) throws PreConditionException {
        try {
            if (newWorker.getOrganization() == null && newWorker.getRole() == null) {
                throw new UnexpectedException("The Worker doesn't have organization and role");
            } else {
                //Hopi
                newWorker.setEnable(true);   //DEFAULT (Enable = true)
                newWorker.setExternalID(newWorker.getId());
                super.doCreate(userID, options, newWorker);
            }
        } catch (UnknownIdException | PermissionDeniedException ex) {
            Logger.getLogger(WorkerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void doUpdate(final Long userID, final Map<String, Object> options, Worker newWorker) {

        try {
            Worker savedWorker = super.doRetrieve(userID, options, newWorker.getId());

            //Hopi
            newWorker.setExternalID(savedWorker.getExternalID());
            newWorker.setEnable(true);
            newWorker.setOrganization(savedWorker.getOrganization());
            super.doUpdate(userID, options, newWorker);

        } catch (UnknownIdException | PreConditionException ex) {
            Logger.getLogger(WorkerController.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (UnknownIdException ex) {
            Logger.getLogger(WorkerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Helpers
     */
    @Override
    public Boolean assertPremissionsCreate(Long userID, Worker entity) throws PermissionDeniedException {
        if (entity.getId() == null && userID != null) {
            return true;
        }
        throw new PermissionDeniedException();
    }

    @Override
    public Boolean assertPremissionsRead(Long userID, Worker entity) throws PermissionDeniedException {
        if (userID != null) {
            return true;
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public Boolean assertPremissionsUpdateDelete(Long userID, Worker entity) throws PermissionDeniedException {
        try {
            Worker savedWorker = super.doRetrieve(userID, new HashMap<>(0), entity.getId());
            if (savedWorker.getEnable() == true) {
                return true;
            } else {
                throw new PermissionDeniedException();
            }
        } catch (UnknownIdException | PreConditionException ex) {
            Logger.getLogger(WorkerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
