package eu.lpinto.universe.util;

import java.io.IOException;
import java.io.InputStream;
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

    /* Email */
    static public final String SUPPORT_ADDR;
    static public final String SENDER_ADDR;
    static public final String SENDER_PASS;
    static public final String SMTP_ADDR;
    static public final Integer SMTP_PORT;

    /* Images (upload) */
    static public final String AVATAR_FOLDER;
    static public final String AVATAR_URL_PREFIX;
    static public final String AVATAR_DEFAULT_FILE_NAME;

    /* REST api */
    static public final String APP_NAME;
    static public final String REST_BASE_URI = "api";
    static public final String REST_SERVICES_PACKAGE = "eu.lpinto.universe.api.services";
    static public final String REST_FILTERS_PACKAGE = "eu.lpinto.universe.api.filters";

    /*
     * Errors
     */
    static public final String ERRORS_EMAIL_ADDR;

    static {
        try (InputStream inputStream = UniverseFundamentals.class.getClassLoader().getResourceAsStream(FILE_PATH);) {

            if (inputStream == null) {
                throw new AssertionError("Missing config file: " + FILE_PATH);
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            ENV = properties.getProperty("ENVIROMENT");

            APP_NAME = properties.getProperty("APP_NAME");
            if (APP_NAME == null) {
                throw new AssertionError("Missing property: APP_NAME");
            }

            /* Email */
            SUPPORT_ADDR = properties.getProperty("support_addr");
            SENDER_ADDR = properties.getProperty("email_addr");
            SENDER_PASS = properties.getProperty("email_pass");
            SMTP_ADDR = properties.getProperty("smtp");
            SMTP_PORT = properties.getProperty("port") == null ? null : Integer.valueOf(properties.getProperty("port"));

            /*
             * Avatars
             */
            String folder = properties.getProperty("DATA_STORE_FOLDER");
            String prefix = properties.getProperty("IMAGES_URL");
            String defaultName = properties.getProperty("AVATAR_FILE_NAME");

            if (folder != null || prefix != null || defaultName != null) {
                if (folder == null || prefix == null || defaultName == null) {
                    throw new AssertionError("Bad configuration for avatar properties: DATA_STORE_FOLDER | IMAGES_URL | AVATAR_FILE_NAME");
                }
            }
            AVATAR_FOLDER = folder;
            AVATAR_URL_PREFIX = prefix;
            AVATAR_DEFAULT_FILE_NAME = defaultName;

            HOST = properties.getProperty("HOST");
            VERSION = properties.getProperty("VERSION");

            /*
             * Erroes
             */
            ERRORS_EMAIL_ADDR = properties.getProperty("errors_email_addr");

        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private UniverseFundamentals() {
        throw new AssertionError("Private Constructor.");
    }

    public interface Enviroments {

        static public final String DEV = "dev";
        static public final String QA = "qa";
        static public final String PROD = "prod";
    }
}
