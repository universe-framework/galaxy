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

    public static final String FILE_PATH = "universe.properties";

    public static final String ENV;
    public static final String HOST;

    /* Images (upload) */
    public static final String AVATAR_FOLDER;
    public static final String AVATAR_URL_PREFIX;
    public static final String AVATAR_DEFAULT_FILE_NAME;

    /* REST api */
    public static final String APP_NAME;
    public static final String REST_BASE_URI = "api";
    public static final String REST_SERVICES_PACKAGE = "eu.lpinto.universe.api.services";
    public static final String REST_FILTERS_PACKAGE = "eu.lpinto.universe.api.filters";

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

        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private UniverseFundamentals() {
        throw new AssertionError("Private Constructor.");
    }

    public interface Enviroments {

        public static final String DEV = "dev";
        public static final String QA = "qa";
        public static final String PROD = "prod";
    }
}
