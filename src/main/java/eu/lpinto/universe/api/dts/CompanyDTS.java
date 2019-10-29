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
                    entity.getPhone(),
                    entity.getFacebook(),
                    entity.getEmail(),
                    entity.getVatNumber(),
                    entity.getCustomField(),
                    entity.getStreet(),
                    entity.getZip(),
                    entity.getTown(),
                    entity.getCountry(),
                    ImageDTS.id(entity.getSelectedAvatar()),
                    PlanDTS.id(entity.getPlan()),
                    CompanyDTS.id(entity.getParent()),
                    AbstractDTS.abstractIDs(entity.getChildren()),
                    ImageDTS.T.ids(entity.getAvatars()),
                    entity.getId(),
                    entity.getName(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getDeleted()
            );

        } else {
            return new eu.lpinto.universe.api.dto.Company(
                    entity.getPhone(),
                    entity.getFacebook(),
                    entity.getEmail(),
                    entity.getVatNumber(),
                    entity.getCustomField(),
                    entity.getStreet(),
                    entity.getZip(),
                    entity.getTown(),
                    entity.getCountry(),
                    null,
                    PlanDTS.id(entity.getPlan()),
                    null,
                    null,
                    null,
                    entity.getId(),
                    entity.getName(),
                    null,
                    entity.getCreated(),
                    null,
                    entity.getUpdated(),
                    entity.getDeleted()
            );
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
        return new Company(dto.getPhone(),
                           dto.getFacebook(),
                           dto.getEmail(),
                           dto.getVatNumber(),
                           dto.getCustomField(),
                           dto.getStreet(),
                           dto.getZip(),
                           dto.getTown(),
                           dto.getCountry(),
                           ImageDTS.T.toDomain(dto.getAvatar()),
                           PlanDTS.T.toDomain(dto.getPlan()),
                           CompanyDTS.T.toDomain(dto.getParent()),
                           null,
                           null,
                           null,
                           dto.getId(),
                           dto.getName(),
                           UserDTS.T.toDomain(dto.getCreator()),
                           dto.getCreated(),
                           UserDTS.T.toDomain(dto.getUpdater()),
                           dto.getUpdated(),
                           dto.getDeleted());
    }
}
