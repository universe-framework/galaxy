package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.api.dto.UniverseDTO;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <E> A domain entity class type.
 * @param <D> A rest dto class type.
 */
public abstract class AbstractDTS<E extends UniverseEntity, D extends UniverseDTO> {

    /*
     * to API
     */
    public static List<Long> abstractIDs(final List<? extends UniverseEntity> input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        List<Long> result = new ArrayList<>(input.size());

        for (UniverseEntity elem : input) {
            result.add(id(elem));
        }

        return result;
    }

    public static Long id(final UniverseEntity entities) {
        if (entities == null) {
            return null;
        }

        return entities.getId();
    }

    public List<Long> ids(final List<E> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }

        List<Long> result = new ArrayList<>(entities.size());

        for (E entity : entities) {
            result.add(entity.getId());
        }

        return result;
    }

    // full objects
    public List<D> toAPI(final List<E> entities) {
        if (entities == null) {
            return null;
        }

        List<D> result = new ArrayList<>(entities.size());

        for (E entity : entities) {
            result.add(toAPI(entity));
        }

        return result;
    }

    public abstract D toAPI(final E entity);


    /*
     * to Domain
     */
    public List<E> toDomainIDs(final List<Long> dtoIDs) {
        if (dtoIDs == null) {
            return null;
        }

        List<E> result = new ArrayList<>(dtoIDs.size());

        for (Long id : dtoIDs) {
            result.add(toDomain(id));
        }

        return result;
    }

    public abstract E toDomain(final Long id);

    /*
     * full objects
     */
    public List<E> toDomain(final List<D> dtos) {
        if (dtos == null) {
            return null;
        }

        List<E> result = new ArrayList<>(dtos.size());

        for (D entity : dtos) {
            result.add(toDomain(entity));
        }

        return result;
    }

    public abstract E toDomain(final D dto);
}
