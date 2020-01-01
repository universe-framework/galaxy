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
    public eu.lpinto.universe.api.dto.Feature buildDTO(Feature entity) {
        if (entity == null) {
            return null;

        } else if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Feature(
                    PlanFeatureDTS.toApiID(entity.getPlans()),
                    entity.getName(),
                    AbstractDTS.toApiID(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.toApiID(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());

        } else {
            return new eu.lpinto.universe.api.dto.Feature(
                    null,
                    entity.getName(),
                    AbstractDTS.toApiID(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.toApiID(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());
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
    public Feature buildEntity(eu.lpinto.universe.api.dto.Feature dto) {
        return new Feature(
                PlanFeatureDTS.T.toEntitiesID(dto.getPlans()),
                dto.getId(), dto.getName(), dto.getCreated(), dto.getUpdated());
    }
}
