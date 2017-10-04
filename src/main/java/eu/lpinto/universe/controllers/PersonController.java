package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.persistence.entities.Person;
import eu.lpinto.universe.persistence.facades.AbstractFacade;
import eu.lpinto.universe.persistence.facades.PersonFacade;
import eu.lpinto.universe.persistence.facades.UserFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
public class PersonController extends AbstractControllerCRUD<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @EJB
    private UserFacade userFacade;

    @EJB
    private PersonFacade facade;

    public PersonController() {
        super(Person.class.getCanonicalName());
    }

    public List<Person> FindByPhone(final String number) {
        try {
            /*
             * Preconditions
             */
            List<Person> personPhone = new ArrayList<>();

            List<Person> savedPhones;

            try {
                savedPhones = facade.phones();
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Error in findAllPhones query");
            }

            if (number != null) {
                if (savedPhones != null) {
                    for (int i = 0; i < savedPhones.size(); i++) {
                        if (savedPhones.get(i) != null) {
                            if (savedPhones.get(i).getMobilePhone() != null) {
                                if (savedPhones.get(i).getMobilePhone().contains(number)) {
                                    personPhone.add(savedPhones.get(i));
                                    return personPhone;
                                }
                            }
                            if (savedPhones.get(i).getPhone() != null) {
                                if (savedPhones.get(i).getPhone().contains(number)) {
                                    personPhone.add(savedPhones.get(i));
                                    return personPhone;
                                }
                            }
                        }
                    }
                }
            }

            /*
             * Body
             */
            return personPhone;

        }
        catch (RuntimeException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
            throw new AssertionError("Error in search");
        }
    }

    @Override
    public AbstractFacade<Person> getFacade() {
        return facade;
    }

    @Override
    public Boolean assertPremissionsUpdateDelete(final Long userID, final Person person) throws PermissionDeniedException {
        return userFacade.retrieve(userID).getPerson().equals(person);
    }
}
