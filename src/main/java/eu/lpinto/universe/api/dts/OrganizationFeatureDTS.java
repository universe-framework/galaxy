package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.api.dto.OrganizationFeatureDTO;
import eu.lpinto.universe.persistence.entities.OrganizationFeature;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public class OrganizationFeatureDTS extends AbstractDTS<OrganizationFeature, OrganizationFeatureDTO> {

    public static final OrganizationFeatureDTS T = new OrganizationFeatureDTS();

    @Override
    protected OrganizationFeatureDTO buildDTO(OrganizationFeature entity) {
        if (entity == null) {
            return null;
        }

        return new OrganizationFeatureDTO(
                OrganizationDTS.toApiID(entity.getOrganization()),
                FeatureDTS.toApiID(entity.getFeature())
        );
    }

    @Override
    protected OrganizationFeature toDomain(Long id) {
        return new OrganizationFeature(id);
    }

    @Override
    protected OrganizationFeature buildEntity(OrganizationFeatureDTO dto) {
        return new OrganizationFeature(
                OrganizationDTS.T.toDomain(dto.getOrganization()),
                FeatureDTS.T.toDomain(dto.getFeature())
        );
    }
}
