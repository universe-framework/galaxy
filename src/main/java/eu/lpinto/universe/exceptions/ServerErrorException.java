package eu.lpinto.universe.exceptions;

import javax.ws.rs.core.Response;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public class ServerErrorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServerErrorException(final Response response) {
        super("The server cant process your request. Http status was: " + response.getStatus());
    }
}
