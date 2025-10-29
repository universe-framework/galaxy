package eu.lpinto.universe.exceptions;

import javax.ws.rs.core.Response;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(final Response response) {
        super("The server cant process your request. Http status was: " + response.getStatus());
    }
}
