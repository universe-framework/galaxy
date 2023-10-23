package eu.lpinto.universe.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * System configuration based on a properties file.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public final class UniverseProperties {

    static public final String FILE_PATH = "properties.properties";

    static public final String VERSION;

    /*
     * Errors
     */
    static {
        try(InputStream inputStream = UniverseProperties.class.getClassLoader().getResourceAsStream(FILE_PATH);) {

            if(inputStream == null) {
                throw new AssertionError("Missing config file: " + FILE_PATH);
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            VERSION = properties.getProperty("project.version");

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private UniverseProperties() {
        throw new AssertionError("Private Constructor.");
    }
}
