package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.api.dto.UniverseDTO;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <E> A domain entity class type.
 * @param <D> A rest dto class type.
 */
public abstract class UniverseDTS<E extends UniverseEntity, D extends UniverseDTO> extends DTS<E, D> {

    public List<E> toEntitiesID(final List<Long> dtoIDs) {
        return dtoIDs == null ? null
               : dtoIDs.parallelStream().map(id -> toDomain(id)).collect(Collectors.toList());
    }

    abstract protected E toDomain(final Long id);
}
