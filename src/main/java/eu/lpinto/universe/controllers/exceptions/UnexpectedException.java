package eu.lpinto.universe.controllers.exceptions;

/**
 * Represents an unexpected error in the business layer.
 * Receiving this exception means a problem with the server, maybe a bug in the
 * system.
 *
 * @author Luís Pinto <code>- mail@lpinto.eu</code>
 */
public class UnexpectedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnexpectedException(final String msg) {
        super(msg + ".");
    }
}
