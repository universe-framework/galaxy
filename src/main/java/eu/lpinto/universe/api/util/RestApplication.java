package eu.lpinto.universe.api.util;

import eu.lpinto.universe.util.UniverseFundamentals;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(UniverseFundamentals.REST_BASE_URI) // set the path to REST web services
public class RestApplication extends Application {

}
