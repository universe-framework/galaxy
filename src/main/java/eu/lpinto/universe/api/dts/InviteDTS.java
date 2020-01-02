package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Invite;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class InviteDTS extends AbstractDTS<Invite, eu.lpinto.universe.api.dto.Invite> {

    public static final InviteDTS T = new InviteDTS();

    @Override
    protected eu.lpinto.universe.api.dto.Invite buildDTO(Invite entity) {
        if (entity == null) {
            return null;

        } else if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Invite(
                    CompanyDTS.toApiID(entity.getOrganization().getCompany()),
                    OrganizationDTS.toApiID(entity.getOrganization()),
                    entity.getEmail(),
                    entity.getRole() == null ? null : entity.getRole().ordinal()
            );

        } else {
            return new eu.lpinto.universe.api.dto.Invite(
                    CompanyDTS.toApiID(entity.getOrganization().getCompany()),
                    OrganizationDTS.toApiID(entity.getOrganization()),
                    entity.getEmail(),
                    entity.getRole() == null ? null : entity.getRole().ordinal()
            );
        }
    }

    @Override
    public Invite toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new Invite(id);
    }

    @Override
    protected Invite buildEntity(eu.lpinto.universe.api.dto.Invite dto) {
        return new Invite(
                OrganizationDTS.T.toDomain(dto.getOrganization()),
                dto.getEmail(),
                dto.getName(),
                dto.getBaseUrl(),
                dto.getRole() == null ? null : eu.lpinto.universe.persistence.entities.WorkerProfile.values()[dto.getRole()],
                UserDTS.T.toDomain(dto.getCreator()),
                dto.getCreated(),
                UserDTS.T.toDomain(dto.getUpdater()),
                dto.getUpdated(),
                dto.getId());
    }
}
