package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Plan;

/**
 * Plan DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class PlanDTS extends AbstractDTS<Plan, eu.lpinto.universe.api.dto.Plan> {

    public static final PlanDTS T = new PlanDTS();

    @Override
    public eu.lpinto.universe.api.dto.Plan buildDTO(Plan entity) {
        if (entity == null) {
            return null;

        } else if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Plan(
                    PlanFeatureDTS.toApiID(entity.getFeatures()),
                    entity.getName(),
                    AbstractDTS.toApiID(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.toApiID(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());

        } else {
            return new eu.lpinto.universe.api.dto.Plan(
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
    public Plan toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new Plan(id);
    }

    @Override
    public Plan buildEntity(eu.lpinto.universe.api.dto.Plan dto) {
        return new Plan(
                PlanFeatureDTS.T.toEntitiesID(dto.getFeatures()),
                dto.getId(), dto.getName(), dto.getCreated(), dto.getUpdated());
    }
}
