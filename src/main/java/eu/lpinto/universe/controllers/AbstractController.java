package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnexpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Foundation for all controllers.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public abstract class AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);
    protected static final String EMAIL_USED = "Email address already in use";
    protected static final String MISSING_USERID = "Error: Unknown userID = ";
    protected static final String MISSING_CLINICID = "Error: Unknown clinicID = ";
    protected static final String MISSING_WEIGHT = "Error: Unknown weightID = ";
    protected static final String CLINIC_REMOVED = "Clinic Removed";

    protected static UnexpectedException internalError(final Exception ex) {
        return new UnexpectedException(firstCause(ex));
    }

    protected static String firstCause(final Throwable ex) {
        return ex.getCause() == null ? ex.getClass().getCanonicalName() + ": " + ex.getMessage() : firstCause(ex.getCause());
    }

    protected static boolean isSystemAdmin(final Long userID) {
        if (userID == null) {
            return false;
        }

        return userID == 0;
    }

    protected static PreConditionException missingParameter(final String paramName) {
        return new PreConditionException(paramName, "' cannot be null!");
    }

    protected AbstractController() {
        super();
    }
}
