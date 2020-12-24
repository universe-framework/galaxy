package eu.lpinto.universe.api.services;

import eu.lpinto.universe.util.UniverseFundamentals;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Path("/")
public class RootService {

    static final private Date DEPLOY_TIME = new Date();
    static final Map<String, String> result = new HashMap<>(5);

    {
        result.put("application", UniverseFundamentals.APP_NAME);
        result.put("base-url", "/" + UniverseFundamentals.REST_BASE_URI);
        result.put("version", UniverseFundamentals.VERSION);
        result.put("deploy", new SimpleDateFormat("yyyy.MM.dd HH:mm zzz").format(DEPLOY_TIME));
        result.put("build", "BUILD-ID");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> get() {
        return result;
    }
}
