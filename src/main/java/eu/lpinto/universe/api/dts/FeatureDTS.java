package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Feature;

/**
 * Feature DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class FeatureDTS extends AbstractDTS<Feature, eu.lpinto.universe.api.dto.Feature> {

    public static final FeatureDTS T = new FeatureDTS();

    @Override
    protected eu.lpinto.universe.api.dto.Feature buildDTO(Feature entity) {
        if (entity == null) {
            return null;

        } else if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Feature(
                    PlanFeatureDTS.toApiID(entity.getPlans())
            );

        } else {
            return new eu.lpinto.universe.api.dto.Feature();
        }
    }

    @Override
    public Feature toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new Feature(id);
    }

    @Override
    protected Feature buildEntity(eu.lpinto.universe.api.dto.Feature dto) {
        return new Feature(
                PlanFeatureDTS.T.toEntitiesID(dto.getPlans()),
                dto.getId(), dto.getName(), dto.getCreated(), dto.getUpdated());
    }
}
