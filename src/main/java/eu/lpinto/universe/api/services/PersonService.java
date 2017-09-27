package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dto.Person;
import eu.lpinto.universe.api.dts.PersonDTS;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
    public Response doFind(final UriInfo uriInfo, final Long userID) {
        try {
            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            Map<String, Object> options = new HashMap<>(10);

            String key = "clinic";
            List<String> values = queryParameters.get(key);
            if (values != null && values.size() == 1) {
                try {
                    options.put(key, Long.valueOf(values.get(0)));
                } catch (NumberFormatException ex) {
                    return response(Response.Status.BAD_REQUEST, "Invalid " + key + " id, expected a number fut found [" + values.get(0) + "]");
                }
            }

            key = "profile";
            values = queryParameters.get(key);
            if (values != null && values.size() == 1) {
                try {
                    options.put(key, values.get(0));
                } catch (NumberFormatException ex) {
                    return response(Response.Status.BAD_REQUEST, "Invalid " + key + " id, expected a number fut found [" + values.get(0) + "]");
                }
            }

            return ok(PersonDTS.T.toAPI(controller.find(userID, options)));

        } catch (PermissionDeniedException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return forbidden(userID);

        } catch (PreConditionException ex) {
            return badRequest(ex.getMessage());

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);
        }
    }

    @Override
    protected eu.lpinto.universe.controllers.PersonController getController() {
        return controller;
    }
}
