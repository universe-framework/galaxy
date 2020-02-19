package eu.lpinto.universe.controllers.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a faulty request to the business layer.
 * Receiving this exception means a problem with the request, maybe a bug in the
 * client.
 *
 * @author Luís Pinto <code>- mail@lpinto.eu</code>
 */
public class PreConditionException extends Exception {

    private static final long serialVersionUID = 1L;

    private Map<String, String[]> errors = new HashMap<>(1);

    public PreConditionException() {
    }

    public PreConditionException(final String field, final String... errors) {
        super(field);

        if (errors != null && errors.length != 0) {
            addError(field, errors);
        }
    }

    public PreConditionException addError(final String field, final String... errors) {
        this.errors.put(field, errors);

        return this;
    }

    /*
     * Getters/Setters
     */
    public Map<String, String[]> getErrors() {
        return errors;
    }

    public void setErrors(final Map<String, String[]> errors) {
        this.errors = errors;
    }
}
