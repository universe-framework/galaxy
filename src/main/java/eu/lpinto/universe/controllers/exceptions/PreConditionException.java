package eu.lpinto.universe.controllers.exceptions;

import eu.lpinto.universe.util.StringUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents a faulty request to the business layer. Receiving this exception means a problem with the request, maybe a
 * bug in the client.
 *
 * @author Luís Pinto <code>- mail@lpinto.eu</code>
 */
public class PreConditionException extends Exception {

    private static final long serialVersionUID = 1L;

    private Map<String, String[]> errors = new HashMap<>(1);

    public PreConditionException() {
    }

    public PreConditionException(final String field, String message, final String... errors) {
        super();
        addError(field, message, errors);
    }

    public PreConditionException addError(final String field, final String message, final String... errors) {
        if (errors == null || errors.length == 0) {
            this.errors.put(field, (String[]) Arrays.asList(message).toArray());

        } else {
            String[] aux = new String[errors.length + 1];
            aux[0] = message;

            for(int i = 0; i < errors.length; i++) {
                aux[i + 1] = errors[i];
            }

            this.errors.put(field, aux);
        }

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

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(500);

        Iterator<Map.Entry<String, String[]>> it = getErrors().entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry<String, String[]> e = it.next();
            sb.append(e.getKey()).append(": ").append(StringUtil.buildString(e.getValue()));

            if (it.hasNext()) {
                sb.append(" | ");
            }
        }

        return sb.toString();
    }
}
