package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Person;
import eu.lpinto.universe.api.dts.PersonDTS;
import javax.ejb.EJB;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST CRUD service for Person.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("people")
public class PersonService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.Person, Person, eu.lpinto.universe.controllers.PersonController, PersonDTS> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    @EJB
    private eu.lpinto.universe.controllers.PersonController controller;

    public PersonService() {
        super(PersonDTS.T);
    }

    @Override
    protected eu.lpinto.universe.controllers.PersonController getController() {
        return controller;
    }
}
