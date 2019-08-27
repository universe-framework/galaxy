package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Errors implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Map<String, String[]>> errors = new ArrayList<>(1);

    public Errors() {
    }

    public Errors(final Map<String, String[]> errors) {
        this.errors.add(errors);
    }

    public Errors(final List<Map<String, String[]>> errors) {
        this.errors = errors;
    }

    public Errors(final String field, final String... errors) {
        addError(field, errors);
    }

    public Errors addError(final String field, final String... errors) {
        Map<String, String[]> newError = new HashMap<>(1);
        newError.put(field, errors);
        this.errors.add(newError);

        return this;
    }

    /*
     * Getters/Setters
     */
    public List<Map<String, String[]>> getErrors() {
        return errors;
    }

    public void setErrors(final List<Map<String, String[]>> errors) {
        this.errors = errors;
    }
}
