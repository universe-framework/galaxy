package eu.lpinto.universe.persistence.facades;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.persistence.entities.AbstractEntity;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 * Foundation for all facades.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <T> Type of entity to be managed.
 */
public abstract class AbstractFacade<T> implements Facade<T> {

    private final Class<T> entityClass;

    /*
     * Constructors
     */
    public AbstractFacade(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /*
     * DAO
     */
    @Override
    public List<T> find(final Map<String, Object> options) throws PreConditionException {
        javax.persistence.criteria.CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public T retrieve(final Long id) throws PreConditionException {
        if (id == null) {
            throw new IllegalArgumentException("Cannot perform a retrieve for " + this.entityClass.getSimpleName() + " with id [null]");
        }

        return getEntityManager().find(entityClass, id);
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public Long count() {
        javax.persistence.criteria.CriteriaQuery<Long> cq = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return (Long) q.getSingleResult();
    }

    @Override
    public void create(final List<T> entities) throws PreConditionException {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        for (T entity : entities) {
            create(entity);
        }
    }

    @Override
    public void create(final T entity) throws PreConditionException {
        create(entity, null);
    }

    @Override
    public void create(final T entity, Map<String, Object> options) throws PreConditionException {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot create a new " + this.entityClass.getCanonicalName() + " with [null] object");
        }

        if (entity instanceof AbstractEntity) {
            AbstractEntity abstractEntity = (AbstractEntity) entity;

            Calendar newNow = options == null || options.get("now") == null
                    ? new GregorianCalendar()
                    : (Calendar) options.get("now");
            if (abstractEntity.getCreated() == null) {
                abstractEntity.setCreated(newNow);
            }
            if (abstractEntity.getUpdated() == null) {
                abstractEntity.setUpdated(newNow);
            }
        }

        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    @Override
    public void edit(final T entity) throws PreConditionException {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    @Override
    public void remove(final T entity) throws PreConditionException {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /*
     * Getters / Setters
     */
    protected abstract EntityManager getEntityManager();

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    /*
    * Helpers
     */
    private LocalDate stringToLocalDate(String string) {
        try {
            ISO8601DateFormat df = new ISO8601DateFormat();
            Date createdAfterAux = df.parse(string);
            return Instant.ofEpochMilli(createdAfterAux.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException ex) {
            throw new AssertionError("Cannot parse String '" + string + "' to LocalDate. Please report this!");
        }
    }

    private LocalDateTime stringToLocalDateTime(String string) {
        try {
            ISO8601DateFormat df = new ISO8601DateFormat();
            Date createdAfterAux = df.parse(string);
            return Instant.ofEpochMilli(createdAfterAux.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (ParseException ex) {
            throw new AssertionError("Cannot parse String '" + string + "' to LocalDateTime. Please report this!");
        }
    }

    private Calendar stringToCalendar(String string) {
        try {
            ISO8601DateFormat df = new ISO8601DateFormat();
            Calendar calendar = Calendar.getInstance();

            Date startedAfterAux = df.parse(string);
            calendar.setTime(startedAfterAux);

            return calendar;
        } catch (ParseException ex) {
            throw new AssertionError("Cannot parse String '" + string + "' to Calendar. Please report this!");
        }
    }

}
