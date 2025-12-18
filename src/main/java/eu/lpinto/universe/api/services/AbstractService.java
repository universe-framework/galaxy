package eu.lpinto.universe.api.services;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import eu.lpinto.universe.api.dto.Errors;
import eu.lpinto.universe.api.dto.FaultDTO;
import eu.lpinto.universe.api.util.StatusEmail;
import eu.lpinto.universe.util.StringUtil;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic functionalities for all REST services.
 *
 * @author Luís Pinto <code>- mail@lpinto.eu</code>
 */
public abstract class AbstractService {

    private static final Map<String, String> DO_NOT_TIMEOUT = UniverseFundamentals.DO_NOT_TIMEOUT;
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    /*
     * Logger
     */
    protected static Logger getLogger() {
        return LOGGER;
    }

    protected static void debug(final Exception ex) {
        getLogger().debug(firstCause(ex), ex);
    }

    /*
     * Helpers
     */
    protected static String firstCause(final Throwable ex) {
        if(ex.getCause() == null) {

            String message = ex.getClass().getSimpleName() + ": " + ex.getMessage();

            if(message == null) {
                message = ex.getClass().getSimpleName();
            }

            return message;

        } else {
            return firstCause(ex.getCause());
        }
    }

    protected static Calendar calendar(final String param) {
        try {
            ISO8601DateFormat df = new ISO8601DateFormat();

            Calendar result = Calendar.getInstance();
            Date afterAux = df.parse(param);
            result.setTime(afterAux);

            return result;

        } catch(ParseException ex) {
            return null;
        }
    }

    protected static LocalDate localDate(final String param) {
        try {
            return LocalDate.parse(param);

        } catch(DateTimeParseException ex) {
            return null;
        }
    }

    /*
     * Responses
     */
    protected static Response response(final Response.Status status, Object msg) {
        return Response.status(status).entity(msg).build();
    }

    protected static Response ok(final Object object) {
        return Response.ok(object).build();
    }

    protected static Response created(final UriBuilder url) {
        return Response.status(Response.Status.CREATED).header("Location", url).build();
    }

    protected static Response noContent() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    protected static Response forbidden(final Long userID) {
        return Response.status(Response.Status.FORBIDDEN).entity(new FaultDTO("001", "Forbidden for user: " + userID)).build();
    }

    protected static Response mismatchID(final Long pathID, final Long objID) {
        return unprocessableEntity(new Errors().addError("id", "Path id[" + pathID + "] differs from entity's id[" + objID + "]"));
    }

    protected static Response badRequest(final String msg) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new FaultDTO("002", msg)).build();
    }

    protected static Response unprocessableEntity(final String field, final String errorMsg) {
        return unprocessableEntity(new Errors().addError(field, errorMsg));
    }

    protected static Response unprocessableEntity(final Errors error) {
        return Response.status(422).entity(error).build();
    }

    protected static Response unknown(final Long id) {
        return Response.status(Response.Status.NOT_FOUND).entity(new FaultDTO("003", "Unknown entity [id=" + id + "]")).build();

    }

    protected static Response conflict(final String msg) {
        return Response.status(Response.Status.CONFLICT).entity(new FaultDTO("004", msg)).build();
    }

    protected static Response internalError(final Exception ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Errors("system", firstCause(ex))).build();
    }

    /*
     * Constructtors
     */
    protected AbstractService() {
        super();
    }

    /*
     * Helpers
     */
    protected void addSKeyString(String key, MultivaluedMap<String, String> queryParameters, Map<String, Object> options) throws IllegalArgumentException {
        List<String> keys = queryParameters.get(key);

        try {
            if(keys != null && !keys.isEmpty() && keys.size() == 1) {
                options.put(key, Long.valueOf(keys.get(0)));
            }
        } catch(NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid value: [" + keys.get(0) + "] for option: [" + key + "]");
        }
    }

    protected void addSKeyCalendar(String key, MultivaluedMap<String, String> queryParameters, Map<String, Object> options) throws IllegalArgumentException {
        List<String> keys = queryParameters.get(key);
        if(keys != null && !keys.isEmpty() && keys.size() == 1) {
            Calendar startedAfter = Calendar.getInstance();
            try {
                startedAfter.setTime(new ISO8601DateFormat().parse(keys.get(0)));
            } catch(ParseException ex) {
                throw new IllegalArgumentException("Invalid value for openAfter: " + keys.get(0));
            }
            options.put(key, startedAfter);
        }
    }

    protected Map<String, Object> buildOptions(final UriInfo uriInfo, final Long userID) {
        return buildOptions(new HashMap<>(10 + (uriInfo == null ? 0 : uriInfo.getQueryParameters().size())), uriInfo, userID);
    }

    protected Map<String, Object> buildOptions(final Map<String, Object> options, final UriInfo uriInfo, final Long userID) {
        return buildOptions(options, uriInfo, userID, null);
    }

    protected Map<String, Object> buildOptions(final Map<String, Object> options, final UriInfo uriInfo, final Long userID, HttpHeaders headers) {
        options.put("startMillis", System.currentTimeMillis());
        options.put("request", UUID.randomUUID().toString());

        options.put("user", userID);
        options.put("url", uriInfo.getRequestUri());

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        queryParameters.keySet().forEach(key -> {
            List<String> values = queryParameters.get(key);
            if(values != null && !values.isEmpty()) {
                if(values.size() == 1) {
                    options.put(key, toType(values.get(0)));
                } else {
                    options.put(key, values.stream().map(e -> toType(e)));
                }
            }
        });

        if(headers != null) {
            MultivaluedMap<String, String> hh = headers.getRequestHeaders();
            hh.keySet().forEach(key -> {
                List<String> values = hh.get(key);
                if(values != null && !values.isEmpty() && values.size() == 1) {
                    options.put(key, toType(values.get(0)));
                }
            });
        }

        options.put("service.start", System.currentTimeMillis());

        return options;
    }

    private Object toType(String value) {
        if("true".equalsIgnoreCase(value)) {
            return Boolean.TRUE;

        } else if("false".equalsIgnoreCase(value)) {
            return Boolean.FALSE;

        } else if(value.startsWith("[")) {
            if(value.matches("\\[([0-9]+[, ]*)+\\]")) {
                return Arrays.asList(value.substring(1, value.length() - 1).replaceAll(" ", "").split(","));

            } else {
                return value;
            }

        } else {
            try {
                return Long.valueOf(value);

            } catch(NumberFormatException ex) {
                return value;
            }
        }
    }

    protected void logRequest(final UriInfo uriInfo, Map<String, Object> options, final String methodName) {
        logRequest(uriInfo, options, methodName, null);
    }

    protected void logRequest(final UriInfo uriInfo, Map<String, Object> options, final String methodName, final Object dto) {
        String body;

        if(dto == null) {
            body = "\"null\"";

        } else if(dto instanceof Collection && ((Collection) dto).size() > 3) {
            Collection col = (Collection) dto;
            body = "{ \"list\": \"" + col.iterator().next().getClass().getSimpleName() + "[" + col.size() + "]\" }";

        } else {
            body = StringUtil.toJson(dto);
        }

        LOGGER.debug("\n{"
                     + "\n\t\"type\" : \"request\","
                     + "\n\t\"id\" : \"{}\","
                     + "\n\t\"url\" : \"{}\","
                     + "\n\t\"method\" : \"{}\","
                     + "\n\t\"options\" : {},"
                     + "\n\t\"body\" : {}"
                     + "\n}",
                     options.get("request"),
                     uriInfo.getRequestUri(), // URL
                     methodName,
                     StringUtil.toJson(options),
                     body
        );
    }

    protected void logResponse(final UriInfo uriInfo, final HttpHeaders headers, final Map<String, Object> options, final String methodName, final Object response) {
        String body;

        if(response == null) {
            body = "\"null\"";

        } else if(response instanceof Collection && ((Collection) response).size() > 3) {
            Collection col = (Collection) response;
            body = "{ \"list\": \"" + col.iterator().next().getClass().getSimpleName() + "[" + col.size() + "]\" }";
        } else {
            body = StringUtil.toJson(response);
        }

        Long serviceDuration = null;
        Long controllerDuration = null;
        if(options != null) {
            if(options.containsKey("service.start") && options.containsKey("service.end")) {
                Long serviceEnd = options.get("service.end") == null ? System.currentTimeMillis() : (Long) options.get("service.end");
                serviceDuration = (serviceEnd) - (Long) (options.get("service.start"));
            }
            if(options.containsKey("controller.start") && options.containsKey("controller.end")) {
                controllerDuration = ((Long) options.get("controller.end")) - (Long) (options.get("controller.start"));
            }
        }

        LOGGER.debug("\n{"
                     + "\n\t\"type\" : \"response\","
                     + "\n\t\"id\" : \"{}\","
                     + "\n\t\"url\" : \"{}\","
                     + "\n\t\"method\" : \"{}\","
                     + "\n\t\"duration\" : {"
                     + "\n\t\t\"total\" : {},"
                     + "\n\t\t\"service\" : {},"
                     + "\n\t\t\"controller\" : {}"
                     + "\n\t},"
                     + "\n\t\"options\" : {},"
                     + "\n\t\"body\" : {}"
                     + "\n}",
                     options.get("request"),
                     uriInfo.getRequestUri(),
                     methodName,
                     System.currentTimeMillis() - (Long) options.get("startMillis"),
                     serviceDuration,
                     controllerDuration,
                     StringUtil.toJson(options),
                     body);

        Long duration = System.currentTimeMillis() - (Long) options.get("startMillis");
        if(duration > 10000) {
            for(Map.Entry<String, String> e : DO_NOT_TIMEOUT.entrySet()) {
                if(uriInfo.getRequestUri().toString().contains("/" + e.getKey()) && methodName.equals(e.getValue())) {
                    return;
                }
            }

            StatusEmail.sendExceptionEmail(new TimeoutException("Duration: " + duration), uriInfo, headers, options, response);
        }
    }

    protected String currentMethod() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
