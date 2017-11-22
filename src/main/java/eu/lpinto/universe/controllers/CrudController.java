package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <E> Domain entity
 */
public interface CrudController<E extends UniverseEntity> {

    public List<E> find(final Long userID, final Map<String, Object> options) throws PermissionDeniedException, PreConditionException;

    public E retrieve(final Long userID, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException;

    public E create(final Long userID, final E entity, final Map<String, Object> options) throws UnknownIdException, PermissionDeniedException, PreConditionException;

    public void update(final Long userID, final E entity) throws UnknownIdException, PermissionDeniedException, PreConditionException;

    public void delete(final Long userID, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException;
}
