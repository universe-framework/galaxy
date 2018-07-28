package eu.lpinto.universe.api.services;

import eu.lpinto.universe.controllers.WorkerController;
import eu.lpinto.universe.persistence.entities.Worker;
import eu.lpinto.universe.api.dto.WorkerDTO;
import eu.lpinto.universe.api.dts.WorkerDTS;
import eu.lpinto.universe.api.services.AbstractServiceCRUD;
import javax.ejb.EJB;
import javax.ws.rs.Path;

/**
 *
 * @author Rita Portas
 */
@Path("workers")
public class WorkerService extends AbstractServiceCRUD<Worker, WorkerDTO, WorkerController, WorkerDTS> {

    @EJB
    private WorkerController workerController;

    public WorkerService() {
        super(WorkerDTS.T);
    }

    @Override
    protected WorkerController getController() {
        return workerController;
    }
}
