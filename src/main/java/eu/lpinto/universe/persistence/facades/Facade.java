package eu.lpinto.universe.persistence.facades;

import java.util.List;
import java.util.Map;

/**
 * Foundation for all facades.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <T> Type of entity to be managed.
 */
public interface Facade<T> {

    public List<T> findAll();

    public List<T> find(final Map<String, Object> options);

    public T retrieve(final Long id);

    public void create(final T entity);

    public void create(final T entity, Map<String, Object> options);

    public void edit(final T entity);

    public void remove(final T entity);
}
