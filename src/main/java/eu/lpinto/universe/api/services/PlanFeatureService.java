package eu.lpinto.universe.api.services;

import eu.lpinto.universe.api.dts.PlanFeatureDTS;
import eu.lpinto.universe.controllers.PlanFeatureController;
import eu.lpinto.universe.persistence.entities.Plan;
import eu.lpinto.universe.persistence.entities.PlanFeature;
import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Stateless
@Path("planFeatures")
public class PlanFeatureService extends AbstractServiceCRUD<eu.lpinto.universe.persistence.entities.PlanFeature, eu.lpinto.universe.api.dto.PlanFeature, PlanFeatureController, PlanFeatureDTS> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanFeatureService.class);

    @EJB
    private PlanFeatureController controller;

    public PlanFeatureService() {
        super(PlanFeatureDTS.T);
    }

    @Override
    protected PlanFeatureController getController() {
        return controller;
    }

    @Override
    public Response doFind(final @Context UriInfo uriInfo, final @HeaderParam("userID") Long userID) {
        try {
            MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
            List<String> planIDs = queryParameters.get("plan");

            if (planIDs != null) {
                if (planIDs.isEmpty()) {
                    return response(Response.Status.BAD_REQUEST, "Cannot search PlanFeature by Plan id with empty value");

                } else if (planIDs.size() == 1) {
                    Long planID = Long.valueOf(planIDs.get(0));
                    PlanFeature entity = new PlanFeature();
                    entity.setPlan(new Plan(planID));

                    Map<String, Object> options = new HashMap<>(1);
                    options.put("entity", entity);

                    return ok(PlanFeatureDTS.T.toAPI(controller.find(userID, options)));
                }
            }

            return ok(PlanFeatureDTS.T.toAPI(controller.find(userID, null)));

        } catch (PermissionDeniedException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return forbidden(userID);

        } catch (PreConditionException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return badRequest(ex.getMessage());

        } catch (RuntimeException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return internalError(ex);
        }
    }
}
