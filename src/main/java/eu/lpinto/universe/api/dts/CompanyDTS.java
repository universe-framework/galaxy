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
    public eu.lpinto.universe.api.dto.Company buildDTO(Company entity) {
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
                    ImageDTS.toApiID(entity.getSelectedAvatar()),
                    PlanDTS.toApiID(entity.getPlan()),
                    CompanyDTS.toApiID(entity.getParent()),
                    AbstractDTS.toApiID(entity.getChildren()),
                    ImageDTS.toApiID(entity.getAvatars()),
                    entity.getId(),
                    entity.getName(),
                    AbstractDTS.toApiID(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.toApiID(entity.getUpdater()),
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
                    PlanDTS.toApiID(entity.getPlan()),
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
    public Company buildEntity(eu.lpinto.universe.api.dto.Company dto) {
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
