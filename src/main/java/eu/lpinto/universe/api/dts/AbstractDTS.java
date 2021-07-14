package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.api.dto.AbstractDTO;
import eu.lpinto.universe.persistence.entities.AbstractEntity;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <E> Entity
 * @param <D> DTO
 */
public abstract class AbstractDTS<E extends AbstractEntity, D extends AbstractDTO> extends UniverseDTS<E, D> {

    @Override
    public D toAPI(E entity) {
        D dto = buildDTO(entity);

        dto.setId(entity.getId());
        dto.setName(entity.getName());

        dto.setMigrationCode(entity.getMigrationCode());
        dto.setMigrationId(entity.getMigrationId());

        dto.setCreated(entity.getCreated() == null ? null : entity.getCreated());
        dto.setDeleted(entity.getDeleted() == null ? null : entity.getDeleted());
        dto.setUpdated(entity.getUpdated() == null ? null : entity.getUpdated());

        if (entity.isFull()) {
            dto.setCreator(UserDTS.toApiID(entity.getCreator()));
            dto.setUpdater(UserDTS.toApiID(entity.getUpdater()));
        }

        return dto;
    }

    @Override
    public E toDomain(D dto) {
        E entity = buildEntity(dto);

        entity.setId(dto.getId());

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getCreated() != null) {
            entity.setCreated(dto.getCreated());
        }

        entity.setCreator(UserDTS.T.toDomain(dto.getCreator()));

        if (dto.getDeleted() != null) {
            entity.setDeleted(dto.getDeleted());
        }

        if (dto.getUpdated() != null) {
            entity.setUpdated(dto.getUpdated());
        }
        entity.setUpdater(UserDTS.T.toDomain(dto.getUpdater()));

        entity.setMigrationCode(dto.getMigrationCode());
        entity.setMigrationId(dto.getMigrationId());

        return entity;
    }
}
