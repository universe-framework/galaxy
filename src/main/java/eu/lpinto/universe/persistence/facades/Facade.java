package eu.lpinto.universe.persistence.facades;

import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import java.util.List;
import java.util.Map;

/**
 * Foundation for all facades.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <T> Type of entity to be managed.
 */
public interface Facade<T> {

    public List<T> find(final Map<String, Object> options) throws PreConditionException;

    public T retrieve(final Long id) throws PreConditionException;

    public void create(final T entity) throws PreConditionException;

    public void create(final T entity, Map<String, Object> options) throws PreConditionException;

    public void edit(final T entity) throws PreConditionException;

    public void remove(final T entity) throws PreConditionException;
}
