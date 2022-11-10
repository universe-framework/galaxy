package eu.lpinto.universe.api.services;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.util.StringUtil;
import javax.ejb.Asynchronous;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
@Path("about-you")
public class AboutyouService extends AbstractService {

    @GET
    @Path("you")
    @Asynchronous
    @Produces(MediaType.TEXT_HTML)
    public void retrieve(@Suspended
            final AsyncResponse asyncResponse,
                         final @Context UriInfo uriInfo,
                         final @Context HttpHeaders headers,
                         final @Context HttpServletRequest inRequest,
                         final @HeaderParam("userID") Long userID,
                         final @PathParam("id") Long id) throws PermissionDeniedException {
        String text = "";
        text += "<html>";
        text += "  <head>";
        text += "  </head>";
        text += "  <body>";

        text += "      <h2>";
        text += "        MatchedURIs";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(uriInfo.getMatchedURIs()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        PathParameters";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(uriInfo.getPathParameters()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        PathSegments";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(uriInfo.getPathSegments()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        QueryParameters";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(uriInfo.getQueryParameters()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        AbsolutePathBuilder";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(uriInfo.getAbsolutePathBuilder()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        Headers";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(headers) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        Cookies";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(inRequest.getCookies()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        RemoteAddr";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(inRequest.getRemoteAddr()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        RemoteHost";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(inRequest.getRemoteHost()) + ",";
        text += "      </p>";

        text += "      <h2>";
        text += "        RequestURI";
        text += "      </h2>";
        text += "      <p>";
        text += "      " + StringUtil.toJson(inRequest.getRequestURI());
        text += "      </p>";

        text += "  </body>";
        text += "  </html>";

        asyncResponse.resume(ok(text));
    }

    public void old(@Suspended
            final AsyncResponse asyncResponse,
                    final @Context UriInfo uriInfo,
                    final @Context HttpHeaders headers,
                    final @Context HttpServletRequest inRequest,
                    final @HeaderParam("userID") Long userID,
                    final @PathParam("id") Long id) throws PermissionDeniedException {
        String result = "{\n\t";

        result += "\"MatchedURIs\": ";
        result += StringUtil.toJson(uriInfo.getMatchedURIs()) + ",";

        result += "\"PathParameters\": ";
        result += StringUtil.toJson(uriInfo.getPathParameters()) + ",";

        result += "\"PathSegments\": ";
        result += StringUtil.toJson(uriInfo.getPathSegments()) + ",";

        result += "\"QueryParameters\": ";
        result += StringUtil.toJson(uriInfo.getQueryParameters()) + ",";

        result += "\"AbsolutePathBuilder\": ";
        result += StringUtil.toJson(uriInfo.getAbsolutePathBuilder()) + ",";

        result += "\"Headers\": ";
        result += StringUtil.toJson(headers) + ",";

//        System.out.println(StringUtil.toJson(inRequest.getAttributeNames()));
//        System.out.println(StringUtil.toJson(inRequest.getAuthType()));
        result += "\"Cookies\": ";
        result += StringUtil.toJson(inRequest.getCookies()) + ",";
//        System.out.println(StringUtil.toJson(inRequest.getDispatcherType()));
//        System.out.println(StringUtil.toJson(inRequest.getHeaderNames()));
//        System.out.println(StringUtil.toJson(inRequest.getLocales()));
//        System.out.println(StringUtil.toJson(inRequest.getParameterMap()));
//        System.out.println(StringUtil.toJson(inRequest.getPathInfo()));

        result += "\"RemoteAddr\": ";
        result += StringUtil.toJson(inRequest.getRemoteAddr()) + ",";

        result += "\"RemoteHost\": ";
        result += StringUtil.toJson(inRequest.getRemoteHost()) + ",";
//        System.out.println(StringUtil.toJson(inRequest.getRemotePort()));
//        System.out.println(StringUtil.toJson(inRequest.getRemoteUser()));

        result += "\"RequestURI\": ";
        result += StringUtil.toJson(inRequest.getRequestURI());
        result += "}";

        /* return */
        asyncResponse.resume(ok(result));
    }
}
