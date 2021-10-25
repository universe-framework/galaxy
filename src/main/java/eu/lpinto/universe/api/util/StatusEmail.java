package eu.lpinto.universe.api.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eu.lpinto.universe.persistence.facades.EmailFacade;
import eu.lpinto.universe.util.UniverseFundamentals;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class StatusEmail implements Runnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(StatusEmail.class);
    static private EmailFacade facade = new EmailFacade();

    /*
     * Public send Exeption Email
     */
    static public void sendExceptionEmail(final Exception ex,
                                          final UriInfo uriInfo,
                                          final HttpHeaders headers,
                                          final Map<String, Object> options) {
        Thread t = new Thread(new StatusEmail(ex, uriInfo, headers, options));
        t.start();
    }

    static public void sendExceptionEmail(final Exception ex,
                                          final UriInfo uriInfo,
                                          final HttpHeaders headers,
                                          final Map<String, Object> options,
                                          final Object dto) {
        Thread t = new Thread(new StatusEmail(ex, uriInfo, headers, options, dto));
        t.start();
    }

    static public void sendExceptionEmail(final Exception ex,
                                          final Map<String, String> uriInfo,
                                          final Map<String, String> headers,
                                          final Map<String, Object> options,
                                          final Object dto) {
        Thread t = new Thread(new StatusEmail(ex, uriInfo, headers, options, dto));
        t.start();
    }

    static private String sendExceptionEmailAux(final Exception ex,
                                                final Map<String, String> uriInfo,
                                                final Map<String, String> headers,
                                                final Map<String, Object> options,
                                                final Object dto) {
        StringBuilder sb = new StringBuilder(1000);

        ExceptionDescription exDescription = excetionHtmlParagraph(ex);

        if (dto != null) {
            sb.append("<div class=\"jumbotron\">\n");
            sb.append("\n<h1>").append(exDescription.className).append(": ").append(exDescription.message).append("</h1>\n");
            sb.append("<p>").append(exDescription.line).append("</p>\n");

            sb.append("<hr>\n");
            sb.append("<div class=\"alert alert-success\">").append(toJson(dto)).append("</div>\n");
            sb.append("</div>\n");

        } else {
            sb.append("\n<h2>").append(exDescription.className).append(": ").append(exDescription.message).append("</h2>\n");
            sb.append("<p>").append(exDescription.line).append("</p>\n");
        }

        sb.append("<hr>\n");
        sb.append(exDescription.html);

        String subject = UniverseFundamentals.APP_NAME == null ? "" : "[" + UniverseFundamentals.APP_NAME + "] ";

        subject += exDescription.className + ": " + exDescription.message;

        return sendStatusEmail(uriInfo, headers, options,
                               subject, // subject
                               "<div class=\"alert alert-danger\">\n" + "<h2>"
                               + "<a class=\"text-danger\" target=\"_blank\" href=\"https://www.google.pt/search?q=" + exDescription.className + ": " + exDescription.message + "\">"
                               + exDescription.className + ": " + exDescription.message + "</a>"
                               + "</h2>\n<p>" + exDescription.line + "</p>\n</div>\n", // header
                               sb.toString()); // message
    }

    /*
     * Send Status Email
     */
    static private String sendStatusEmail(final Map<String, String> uriInfo,
                                          final Map<String, String> headers,
                                          final Map<String, Object> options,
                                          final String subject,
                                          final String header,
                                          final String message) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append("<!DOCTYPE html>\n");
        sb.append("<HTML lang=\"en\">\n");
        sb.append("<HEAD>\n");
        sb.append("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">\n");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
        sb.append("</HEAD>\n");
        sb.append("<BODY>\n");
        sb.append("<div class=\"container-fluid\">\n");
        sb.append("<div class=\"page-header\">\n");
        if (UniverseFundamentals.APP_NAME != null) {
            sb.append("<h1>").append(UniverseFundamentals.APP_NAME).append("</h1>\n");
        }
        sb.append("</div>");
        sb.append(header);

        sb.append("<div class=\"row\">\n");

        /*
         * Options
         */
        sb.append("<div class=\"col-xs-12 col-md-6\">\n");
        sb.append("\n<div class=\"panel panel-primary\">\n<div class=\"panel-heading\">Options: </div>\n<div class=\"panel-body\">\n");
        sb.append("<ul class=\"list-group\">\n");
        options.entrySet().forEach((e) -> {
            sb.append("\t<li class=\"list-group-item\"><b>").append(e.getKey()).append("</b> : ").append(e.getValue()).append("</li><br>\n");
        });
        sb.append("</ul></div></div>\n");
        sb.append("</div>\n");

        /*
         * Query Params
         */
        sb.append("<div class=\"col-xs-12 col-md-6\">\n");
        sb.append("\n<div class=\"panel panel-primary\">\n<div class=\"panel-heading\">Query params: </div>\n<div class=\"panel-body\">\n");
        sb.append("<ul class=\"list-group\">\n");
        uriInfo.entrySet().forEach((e) -> {
            sb.append("\t<li class=\"list-group-item\"><b>").append(e.getKey()).append("</b> : ").append(e.getValue()).append("</li><br>\n");
        });
        sb.append("</ul></div></div>\n");
        sb.append("</div>\n");

        /*
         * header
         */
        sb.append("<div class=\"col-xs-12\">\n");
        sb.append("\n<div class=\"panel panel-primary\">\n<div class=\"panel-heading\">Headers: </div>\n<div class=\"panel-body\">\n");
        sb.append("<ul class=\"list-group\">\n");
        headers.entrySet().forEach((e) -> {
            sb.append("\t<li class=\"list-group-item\"><b>").append(e.getKey()).append("</b> : ").append(e.getValue()).append("</li><br>\n");
        });
        sb.append("</ul></div></div>\n");
        sb.append("</div>\n");

        sb.append("</div>\n");

        /*
         * Body Message
         */
        sb.append(message);

        sb.append("</div>\n");
        sb.append("</BODY>\n");
        sb.append("</HTML>");

        String emailBody = sb.toString();

        sendEmail(subject, emailBody);

        return emailBody;
    }

    /*
     * Do Send
     */
    static public void sendEmail(final String subject, final String emailMessage) {
        facade.sendEmail(subject, emailMessage);
    }

    /*
     * Content helpers
     */
    static private ExceptionDescription excetionHtmlParagraph(final Exception ex) {

        ExceptionDescription result = new ExceptionDescription();

        /* Original cause */
        String innerCause = ex.getMessage();
        String innerClass = ex.getClass().getSimpleName();
        String innerLine = "";

        StringWriter sw = new StringWriter();
        PrintWriter printWriter = new PrintWriter(sw);

        /* Print Exception */
        printWriter.println("\n<h4>Exception: " + ex.getClass().getCanonicalName() + ": " + ex.getMessage() + "</h4>");
        printWriter.println("\n<small>");
        for (StackTraceElement e : ex.getStackTrace()) {
            printWriter.println("\tat " + e.getClassName() + "." + e.getMethodName() + "(" + e.getFileName() + ":" + e.getLineNumber() + ")<br>");
        }
        printWriter.println("</small>");

        for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
            printWriter.println("<h4>Caused by: " + t.getClass().getSimpleName() + ": " + t.getMessage() + "</h4>");

            printWriter.println("<small>");
            for (StackTraceElement elem : t.getStackTrace()) {
                printWriter.println("\tat " + elem.getClassName() + "." + elem.getMethodName() + "(" + elem.getFileName() + ":" + elem.getLineNumber() + ")<br>");
            }
            printWriter.println("</small>");

            innerCause = t.getMessage();
            innerClass = t.getClass().getSimpleName();
            StackTraceElement elem0 = t.getStackTrace()[0];
            innerLine = elem0.getClassName() + "." + elem0.getMethodName() + "(" + elem0.getFileName() + ":" + elem0.getLineNumber() + ")";
        }

        result.className = innerClass;
        result.message = innerCause;
        result.line = innerLine;
        result.html = sw.toString();

        return result;
    }

    static private String toJson(final Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        /*
         * Serialization
         */
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.enable(SerializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return mapper.writeValueAsString(obj).replace(System.lineSeparator(), System.lineSeparator() + '\t');
        } catch (JsonProcessingException ex) {
            LOGGER.error("Cannot serialize object: " + obj);
            return "[cannot serialize]";
        }
    }

    private Exception ex;
    private Map<String, String> uriInfo;
    private Map<String, String> headers;
    private Map<String, Object> options;
    private Object dto;

    /*
     * Constructors
     */
    public StatusEmail(final Exception ex,
                       final UriInfo uriInfo,
                       final HttpHeaders headers,
                       final Map<String, Object> options) {
        this(ex, uriInfo, headers, options, null);
    }

    public StatusEmail(final Exception ex,
                       final UriInfo uriInfo,
                       final HttpHeaders headers,
                       final Map<String, Object> options,
                       final Object dto) {
        if (uriInfo != null && uriInfo.getQueryParameters() != null) {
            Map<String, String> uriInfoAux = new HashMap<>(uriInfo.getQueryParameters().size());

            uriInfo.getQueryParameters().entrySet().forEach((e) -> {
                uriInfoAux.put(e.getKey(), String.valueOf(e.getValue()));
            });

            this.uriInfo = uriInfoAux;
        }

        if (headers != null && headers.getRequestHeaders() != null) {
            Map<String, String> headersAux = new HashMap<>(headers.getRequestHeaders().size());
            headers.getRequestHeaders().entrySet().forEach((e) -> {
                headersAux.put(e.getKey(), String.valueOf(e.getValue()));
            });

            this.headers = headersAux;
        }

        this.ex = ex;
        this.options = options;
        this.dto = dto;
    }

    public StatusEmail(final Exception ex,
                       final Map<String, String> uriInfo,
                       final Map<String, String> headers,
                       final Map<String, Object> options,
                       final Object dto) {
        this.ex = ex;
        this.uriInfo = uriInfo;
        this.headers = headers;
        this.options = options;
        this.dto = dto;
    }

    /*
     * Methods
     */
    @Override
    public void run() {
        if (headers != null && headers.containsKey("Referer") && !headers.get("Referer").contains("localhost") && !headers.get("Referer").contains("dev")) {
            sendExceptionEmailAux(ex, uriInfo, headers, options, dto);
        }
    }

    /*
     * Aux Class
     */
    static private class ExceptionDescription {

        String message;
        String className;
        String line;
        String html;
    }
}
