package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Invite;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class InviteDTS extends AbstractDTS<Invite, eu.lpinto.universe.api.dto.Invite> {

    public static final InviteDTS T = new InviteDTS();

    @Override
    public eu.lpinto.universe.api.dto.Invite toAPI(Invite entity) {
        if (entity == null) {
            return null;

        } else if (entity.isFull()) {
            return new eu.lpinto.universe.api.dto.Invite(
                    CompanyDTS.id(entity.getCompany()),
                    entity.getEmail(),
                    entity.getName(),
                    entity.getBaseUrl(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());

        } else {
            return new eu.lpinto.universe.api.dto.Invite(
                    CompanyDTS.id(entity.getCompany()),
                    entity.getEmail(),
                    entity.getName(),
                    entity.getBaseUrl(),
                    AbstractDTS.id(entity.getCreator()),
                    entity.getCreated(),
                    AbstractDTS.id(entity.getUpdater()),
                    entity.getUpdated(),
                    entity.getId());
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
    public Invite toDomain(eu.lpinto.universe.api.dto.Invite entity) {
        return new Invite(
                CompanyDTS.T.toDomain(entity.getCompany()),
                entity.getEmail(),
                entity.getName(),
                entity.getBaseUrl(),
                UserDTS.T.toDomain(entity.getCreator()),
                entity.getCreated(),
                UserDTS.T.toDomain(entity.getUpdater()),
                entity.getUpdated(),
                entity.getId());
    }
}
