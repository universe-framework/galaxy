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
    public eu.lpinto.universe.api.dto.Plan toAPI(Plan entity) {
        if (entity == null) {
            return null;

        } else if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Plan(
                    PlanFeatureDTS.T.ids(entity.getFeatures()),
                    entity.getName(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());

        } else {
            return new eu.lpinto.universe.api.dto.Plan(
                    null,
                    entity.getName(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
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
    public Plan toDomain(eu.lpinto.universe.api.dto.Plan dto) {
        return new Plan(
                PlanFeatureDTS.T.toDomainIDs(dto.getFeatures()),
                dto.getId(), dto.getName(), dto.getCreated(), dto.getUpdated());
    }
}
