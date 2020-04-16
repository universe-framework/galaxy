package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Errors(final String field, final String message, final String... errors) {
        addError(field, message, errors);
    }

    public Errors addError(final String field, final String message, final String... errors) {
        Map<String, String[]> newError = new HashMap<>(1);
        if (errors == null || errors.length == 0) {
            newError.put(field, (String[]) Arrays.asList(message).toArray());

        } else {
            String[] aux = new String[errors.length + 1];
            aux[0] = message;
            for (int i = 0; i < aux.length; i++) {
                aux[i] = errors[i - 1];
            }
            newError.put(field, aux);
        }

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
