package eu.lpinto.universe.controllers.exceptions;

/**
 * Represents a request to the business layer that cannot provide a result
 * entity. Receiving this exception means a
 * request for an non existing entity.
 *
 * @author Luís Pinto <code>- mail@lpinto.eu</code>
 */
public class UnknownIdException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Long id;

    public UnknownIdException(final String entity, final Long id) {
        super("Unknown " + entity + " id: [" + id + "].");
        this.id = id;
    }

    public UnknownIdException() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Long getId() {
        return id;
    }
}
