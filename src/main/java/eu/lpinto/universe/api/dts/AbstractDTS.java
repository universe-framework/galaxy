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

//    @Override
//    public D toDTO(E entity) {
//        D dto = super.toDTO(entity);
//
//        dto.setMigrationCode(entity.getMigrationCode());
//        dto.setMigrationId(entity.getMigrationId());
//
//        return dto;
//    }
//
//    @Override
//    public E toEntity(D dto) {
//        E entity = super.toEntity(dto);
//
//        entity.setMigrationCode(dto.getMigrationCode());
//        entity.setMigrationId(dto.getMigrationId());
//
//        return entity;
//    }
}
