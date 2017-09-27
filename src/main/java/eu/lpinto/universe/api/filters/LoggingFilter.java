package eu.lpinto.universe.api.filters;

import java.io.IOException;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.ext.Provider;

/**
 * REST Filter that prints requests and responses headers and bodies.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Provider
@Priority(2)
public class LoggingFilter extends org.glassfish.jersey.filter.LoggingFilter {

    public LoggingFilter() {
        super(Logger.getLogger(LoggingFilter.class.getName()), true);
    }

    /*
     * Request
     */
    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        if ("GET".equals(requestContext.getMethod()) && requestContext.getUriInfo().getAbsolutePath().toString().endsWith("api")) {
            return;
        }

        if ("GET".equals(requestContext.getMethod()) && requestContext.getUriInfo().getAbsolutePath().toString().endsWith("api/")) {
            return;
        }

        if ("POST".equals(requestContext.getMethod()) && requestContext.getUriInfo().getPath().endsWith("users")) {
            return;
        }

        if ("POST".equals(requestContext.getMethod()) && requestContext.getUriInfo().getPath().endsWith("tokens")) {
            return;
        }

        super.filter(requestContext);
    }

    /*
     * Response
     */
    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

        if ("GET".equals(requestContext.getMethod())
            && requestContext.getUriInfo().getAbsolutePath().toString().endsWith("api")) {
            return;
        }

        if ("GET".equals(requestContext.getMethod())
            && requestContext.getUriInfo().getAbsolutePath().toString().endsWith("api/")) {
            return;
        }

        super.filter(requestContext, responseContext);
    }
}
