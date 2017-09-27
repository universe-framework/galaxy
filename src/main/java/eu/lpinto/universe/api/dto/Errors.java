package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Errors implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String[]> errors = new HashMap<>(1);

    public Errors() {
    }

    public Errors(final Map<String, String[]> errors) {
        this.errors = errors;
    }

    public Errors(final String field, final String... errors) {
        addError(field, errors);
    }

    public Errors addError(final String field, final String... errors) {
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
