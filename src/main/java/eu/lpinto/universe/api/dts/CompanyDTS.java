package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Company;

/**
 * Company DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class CompanyDTS extends AbstractDTS<Company, eu.lpinto.universe.api.dto.Company> {

    public static final CompanyDTS T = new CompanyDTS();

    @Override
    public eu.lpinto.universe.api.dto.Company toAPI(Company entity) {
        if (entity == null) {
            return null;
        }

        if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Company(
                    entity.getPhone(), entity.getFacebook(), entity.getEmail(), entity.getVatNumber(), ImageDTS.id(entity.getSelectedAvatar()),
                    PlanDTS.id(entity.getPlan()), CompanyDTS.id(entity.getParent()),
                    AbstractDTS.abstractIDs(entity.getChildren()), ImageDTS.T.ids(entity.getAvatars()),
                    entity.getName(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());

        } else {
            return new eu.lpinto.universe.api.dto.Company(
                    entity.getPhone(), entity.getFacebook(), entity.getEmail(), entity.getVatNumber(), ImageDTS.id(entity.getSelectedAvatar()),
                    PlanDTS.id(entity.getPlan()), CompanyDTS.id(entity.getParent()),
                    null, null,
                    entity.getName(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());
        }
    }

    @Override
    public Company toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new Company(id);
    }

    @Override
    public Company toDomain(eu.lpinto.universe.api.dto.Company dto) {
        return new Company(dto.getPhone(), dto.getFacebook(), dto.getEmail(), dto.getVatNumber(), ImageDTS.T.toDomain(dto.getAvatar()),
                                PlanDTS.T.toDomain(dto.getPlan()), CompanyDTS.T.toDomain(dto.getParent()),
                                null, null,
                                dto.getId(), dto.getName(), dto.getCreated(), dto.getUpdated());
    }
}
