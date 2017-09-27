package eu.lpinto.universe.controllers.exceptions;

/**
 * Represents a request to a not authorized business function.
 * Receiving this exception means the requester user has no permissions to
 * complete the request.
 *
 * @author Luís Pinto <code>- mail@lpinto.eu</code>
 */
public class PermissionDeniedException extends Exception {

    private static final long serialVersionUID = 1L;

    public PermissionDeniedException() {
        super();
    }
}
