package eu.lpinto.universe.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * System configuration based on a properties file.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public final class UniverseFundamentals {

    static public final String FILE_PATH = "universe.properties";

    static public final String ENV;
    static public final String HOST;
    static public final String VERSION;

    static public final Integer DEFAULT_PLAN;
    static public final String IMPORTS_FOLDER;

    /* Email */
    static public final String SUPPORT_ADDR;
    static public final String REPLY_TO;
    static public final String SENDER_NAME;
    static public final String SENDER_ADDR;
    static public final String SENDER_PASS;
    static public final String SMTP_ADDR;
    static public final Integer SMTP_PORT;

    /* Images (upload) */
    static public final String AVATAR_FOLDER;
    static public final String AVATAR_URL_PREFIX;

    /* REST api */
    static public final String APP_NAME;
    static public final Map<String, String> DMZ;
    static public final String REST_BASE_URI = "api";
    static public final String REST_SERVICES_PACKAGE = "eu.lpinto.universe.api.services";
    static public final String REST_FILTERS_PACKAGE = "eu.lpinto.universe.api.filters";

    /* Auth */
    static public final String AUTH_USER_ID = "userID";
    static public final String AUTH_GOD = "GOD";

    /*
     * Errors
     */
    static public final String ERRORS_EMAIL_ADDR;
    static public final Map<String, String> DO_NOT_TIMEOUT;
    static public final Integer DEBUG_LEVEL;

    static {
        try(InputStream inputStream = UniverseFundamentals.class.getClassLoader().getResourceAsStream(FILE_PATH);) {

            if(inputStream == null) {
                throw new AssertionError("Missing config file: " + FILE_PATH);
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            ENV = properties.getProperty("ENVIROMENT");

            APP_NAME = properties.getProperty("APP_NAME");
            if(APP_NAME == null) {
                throw new AssertionError("Missing property: APP_NAME");
            }

            String dmzAux = properties.getProperty("DMZ");
            if(dmzAux != null) {
                String[] pairs = dmzAux.split(",");
                DMZ = new HashMap<>(pairs.length);

                for(String pair : pairs) {
                    String[] aux = pair.trim().split(":");
                    DMZ.put(aux[0].trim(), aux[1].trim());
                }
            } else {
                DMZ = null;
            }

            IMPORTS_FOLDER = properties.getProperty("IMPORTS_FOLDER");

            DEFAULT_PLAN = properties.getProperty("default_plan") == null ? 1 : Integer.valueOf(properties.getProperty("default_plan"));

            /* Email */
            SUPPORT_ADDR = properties.getProperty("support_addr");
            REPLY_TO = properties.getProperty("reply_to");
            SENDER_NAME = properties.getProperty("sender_name");
            SENDER_ADDR = properties.getProperty("email_addr");
            SENDER_PASS = properties.getProperty("email_pass");
            SMTP_ADDR = properties.getProperty("smtp");
            SMTP_PORT = properties.getProperty("port") == null ? null : Integer.valueOf(properties.getProperty("port"));

            /*
             * Avatars
             */
            String folder = properties.getProperty("DATA_STORE_FOLDER");
            String prefix = properties.getProperty("IMAGES_URL");

            if(folder != null || prefix != null) {
                if(folder == null || prefix == null) {
                    throw new AssertionError("Bad configuration for avatar properties: DATA_STORE_FOLDER | IMAGES_URL");
                }
            }
            AVATAR_FOLDER = folder;
            AVATAR_URL_PREFIX = prefix;

            HOST = properties.getProperty("HOST");
            VERSION = properties.getProperty("VERSION");

            /*
             * Erroes
             */
            ERRORS_EMAIL_ADDR = properties.getProperty("errors_email_addr");

            String timeouts = properties.getProperty("do_not_timeout");
            DO_NOT_TIMEOUT = new HashMap<>(5);
            if(timeouts != null && !timeouts.isEmpty()) {
                String[] pairs = timeouts.split(" ");

                for(String pair : pairs) {
                    String[] split = pair.split(":");

                    DO_NOT_TIMEOUT.put(split[0], split[1]);
                }
            }

            DEBUG_LEVEL = properties.getProperty("debug_level") == null ? 0 : Integer.valueOf(properties.getProperty("debug_level"));

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private UniverseFundamentals() {
        throw new AssertionError("Private Constructor.");
    }

    public interface Enviroments {

        static public final String LOCAL = "local";
        static public final String DEV = "dev";
        static public final String QA = "qa";
        static public final String PROD = "prod";
    }
}
