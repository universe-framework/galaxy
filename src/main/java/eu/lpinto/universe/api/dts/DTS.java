package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.UniverseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author lpint
 * @param <E> Entity
 * @param <D> DTO
 */
public abstract class DTS<E extends UniverseEntity, D> {

    /*
     * To DTO
     */
    static final public List<Long> toApiID(final List<? extends UniverseEntity> entities) {
        return entities == null || entities.isEmpty() ? null
               : entities.parallelStream().map(entity -> DTS.toApiID(entity)).collect(Collectors.toList());
    }

    static final public Long toApiID(final UniverseEntity entities) {
        return entities == null ? null
               : entities.getId();
    }

    public List<D> toAPI(final List<E> entities) {
        if (entities == null) {
            return null;
        }

        List<D> result = new ArrayList<>(entities.size());

        entities.forEach((entity) -> {
            result.add(toAPI(entity));
        });

        return result;
    }

    public D toAPI(final E entity) {
        return buildDTO(entity);
    }

    abstract protected D buildDTO(final E entity);

    /*
     * To Entity
     */
    public List<E> toDomain(final List<D> dtos) {
        return dtos == null ? null
               : dtos.parallelStream().map(entity -> DTS.this.toDomain(entity)).collect(Collectors.toList());
    }

    public E toDomain(final D dto) {
        E result = buildEntity(dto);

        return result;
    }

    abstract protected E buildEntity(final D dto);
}
