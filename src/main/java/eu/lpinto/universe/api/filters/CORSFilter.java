package eu.lpinto.universe.api.filters;

import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Provider
@Priority(1)
public class CORSFilter implements ContainerResponseFilter {

    private static final List<String> ALLOWED_ORIGINS;

    static {
        String cors = UniverseFundamentals.CORS;

        if (cors == null || cors.isEmpty()) {
            ALLOWED_ORIGINS = null;
        } else {
            ALLOWED_ORIGINS = Arrays.asList(cors.trim().replaceAll(" ", "").split(","));
        }
    }

    @Override
    public void filter(final ContainerRequestContext requestContext,
                       final ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaderString("Origin");

        if (ALLOWED_ORIGINS == null) {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", "*");

        } else if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
            responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
        }

        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().putSingle("Vary", "Origin");

        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, u-entity, u-entity-id");
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", "1209600");
    }
}
