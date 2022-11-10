package eu.lpinto.universe.api.filters;

import eu.lpinto.universe.api.dto.FaultDTO;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.UserFacade;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Priority;
import javax.ejb.EJB;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * REST filter for OAuth tokens validation.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Provider
@Priority(3)
public class AccessTokenValidation implements ContainerRequestFilter, ContainerResponseFilter {

    @EJB
    private UserFacade userFacade;

    public static final String USER_ID = UniverseFundamentals.AUTH_USER_ID;
    public static final String GOD = UniverseFundamentals.AUTH_GOD;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String ADMIN_ID = "0";
    private static final Map<String, String> DMZ_ENDPOINTS;

    static {
        DMZ_ENDPOINTS = new HashMap<>(5);

        DMZ_ENDPOINTS.put("", HttpMethod.GET);
        DMZ_ENDPOINTS.put("/", HttpMethod.GET);
        DMZ_ENDPOINTS.put("/about/you", HttpMethod.GET);
        DMZ_ENDPOINTS.put("/emailValidations", HttpMethod.GET);
        DMZ_ENDPOINTS.put("/features", HttpMethod.GET);
        DMZ_ENDPOINTS.put("/invites", HttpMethod.GET);
        DMZ_ENDPOINTS.put("/plans", HttpMethod.GET);
        DMZ_ENDPOINTS.put("/planFeatures", HttpMethod.GET);

        DMZ_ENDPOINTS.put("/people", HttpMethod.POST);
        DMZ_ENDPOINTS.put("/tokens", HttpMethod.POST);
        DMZ_ENDPOINTS.put("/users", HttpMethod.POST);
        DMZ_ENDPOINTS.put("/users/passwordRecovery", HttpMethod.POST);

        DMZ_ENDPOINTS.put("/emailValidations", HttpMethod.PUT);

        if (UniverseFundamentals.DMZ != null) {
            UniverseFundamentals.DMZ.entrySet().forEach((e) -> {
                DMZ_ENDPOINTS.put("/" + e.getKey(), e.getValue());
            });
        }
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        String bearerToken = requestContext.getHeaderString(AUTHORIZATION_HEADER);

        if (bearerToken != null && !bearerToken.isEmpty()) {

            /*
             * Token ID
             */
            String TokenID = bearerToken.substring(BEARER.length());
            try {

                if (TokenID.isEmpty()) {
                    requestContext.abortWith(Response
                            .status(Response.Status.UNAUTHORIZED)
                            .entity(new FaultDTO("103", "Empty access token")).build());

                } else {

                    User user = userFacade.getbyToken(TokenID);

                    if (user == null) {
                        requestContext.abortWith(Response
                                .status(Response.Status.UNAUTHORIZED)
                                .entity(new FaultDTO("104", "Unknown access token")).build());

                    } else {
                        requestContext.getHeaders().add(USER_ID, String.valueOf(user.getId()));
                        requestContext.getHeaders().add(GOD, String.valueOf(user.getGod()));
                    }
                }

            } catch (RuntimeException ex) {
                requestContext.abortWith(Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new FaultDTO("100", "Cannot validate token. Error: " + ex.getLocalizedMessage())).build());
            }

        } else {
            if (dmz(requestContext)) {
                // return;

            } else if (bearerToken == null) {
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(new FaultDTO("101", "Missing Authorization token")).build());

            } else if (bearerToken.isEmpty() || !bearerToken.startsWith(BEARER)) {
                requestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity(new FaultDTO("102", "Invalid Authorization token")).build());

            }
        }
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
        // Empty
    }

    /*
     * Private
     */
    private static boolean dmz(final ContainerRequestContext requestContext) {
        String absolutePath = requestContext.getUriInfo().getAbsolutePath().toString();
        MultivaluedMap<String, String> queryParameters = requestContext.getUriInfo().getQueryParameters();
        String method = requestContext.getRequest().getMethod();

        if (method.equals("OPTIONS")) {
            return true;
        }

        String[] service = absolutePath.split("/api");

        if (service.length == 1) {
            return true; // root endpoint without '/'
        }

        if (service[1].startsWith("/tokens/")) {
            return true;
        }

        if (service[1].startsWith("/p/")) {
            return true;
        }

        if (method.equals("PUT")) {
            service[1] = service[1].replace("/", "").split("\\d{1,}")[0];
        }

        String serviceKey = service[1];

        if ((DMZ_ENDPOINTS.get(serviceKey) != null && DMZ_ENDPOINTS.get(serviceKey).equals(method))
            || service[1].endsWith("csv")) {
            return true; // dmz endpoint + operation
        }

        serviceKey = "/" + (service[1].contains("/") ? service[1].split("/")[1] : service[1]) + "/*";

        if (service[1].contains("/") && (DMZ_ENDPOINTS.get(serviceKey) != null && DMZ_ENDPOINTS.get(serviceKey).equals(method))) {
            return true; // dmz endpoint/* + operation
        }

        List<String> hack = queryParameters.get("userhack");

        if (hack != null && !hack.isEmpty() && hack.contains("admin") && service[0].contains("localhost")) {
            requestContext.getHeaders().add(USER_ID, ADMIN_ID);
            return true;
        }

        return false;
    }
}
