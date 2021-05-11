package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnexpectedException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.AbstractEntity;
import eu.lpinto.universe.persistence.entities.Repeatable;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.Facade;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Foundation controller for CRUD operations
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <E> Domain UniverseEntity
 */
public abstract class AbstractControllerCRUD<E extends UniverseEntity> extends AbstractController implements CrudController<E> {

    /*
     * Helpers
     */
    public static int GetCalendarField(final String periodType) {
        switch (periodType) {
            case "d":
                return Calendar.DAY_OF_MONTH;
            case "w":
                return Calendar.WEEK_OF_YEAR;
            case "m":
                return Calendar.MONTH;
            case "y":
                return Calendar.YEAR;
            default:
                throw new UnsupportedOperationException("Invalid Period Type");
        }
    }

    /*
     * Fields
     */
    private final String entityName;

    /*
     * Constructors
     */
    public AbstractControllerCRUD(final String entityName) {
        super();
        this.entityName = entityName;
    }

    /*
     * CRUD
     */
    @Override
    public List<E> find(final Long userID, final Map<String, Object> options) throws PermissionDeniedException, PreConditionException {
        options.put("controller.start", System.currentTimeMillis());

        Boolean permission = assertPremissionsRead(userID, null);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        List<E> result = doFind(userID, options);

        options.put("controller.end", System.currentTimeMillis());
        return result;
    }

    public List<E> doFind(final Long userID, final Map<String, Object> options) throws PreConditionException {
        return getFacade().find(options);
    }

    @Override
    public E retrieve(final Long userID, final Map<String, Object> options, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        options.put("controller.start", System.currentTimeMillis());

        /*
         * Preconditions
         */
        if (id == null) {
            throw missingParameter("id");
        }

        /*
         * Body
         */
        E result = doRetrieve(userID, options, id);

        if (result == null) {
            throw new UnknownIdException(entityName, id);
        }

        Boolean permission = assertPremissionsRead(userID, result);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        options.put("controller.end", System.currentTimeMillis());
        return result;
    }

    public E doRetrieve(final Long userID, final Map<String, Object> options, final Long id) throws UnknownIdException, PreConditionException {
        E savedEntity;
        savedEntity = getFacade().retrieve(id);
        return savedEntity;
    }

    @Override
    public List<E> create(final Long userID, final Map<String, Object> options, final List<E> entities) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<E> result = new ArrayList<>(entities.size());

        entities.parallelStream().forEach(entity -> {
            try {
                E create = create(userID, options, entity);
                synchronized (result) {
                    result.add(create);
                }
            } catch (UnknownIdException | PermissionDeniedException | PreConditionException ex) {
                throw new UnexpectedException(ex.getMessage());
            }
        });

        return result;
    }

    @Override
    public E create(final Long userID, final Map<String, Object> options, final E entity) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        options.put("controller.start", System.currentTimeMillis());

        Boolean permission = assertPremissionsCreate(userID, entity);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        if (userID != null && entity instanceof AbstractEntity) {
            AbstractEntity abstractEntity = (AbstractEntity) entity;
            abstractEntity.setCreator(new User(userID));
        }

        doCreate(userID, options, entity);

        options.put("controller.end", System.currentTimeMillis());
        return entity;
    }

    public void doCreate(final Long userID, final Map<String, Object> options, final E entity) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        getFacade().create(entity);
    }

    public void doCreateRepetitions(E entity, Long userID, Map<String, Object> options) throws PreConditionException, PermissionDeniedException, UnknownIdException {
        if (entity instanceof Repeatable) {
            Repeatable newEntity = (Repeatable) entity;
            if (newEntity.getPeriod() != null && newEntity.getPeriodType() != null && newEntity.getPeriodUntil() != null) {
                Calendar s = newEntity.getStart();
                Calendar e = newEntity.getEnd();

                s = (Calendar) s.clone();
                s.add(GetCalendarField(newEntity.getPeriodType()), newEntity.getPeriod());
                e = (Calendar) e.clone();
                e.add(GetCalendarField(newEntity.getPeriodType()), newEntity.getPeriod());

                while (s.before(newEntity.getPeriodUntil())) {
                    E aux = (E) newEntity.clone(newEntity, s, e);
                    doCreate(userID, options, aux);

                    s = (Calendar) s.clone();
                    s.add(GetCalendarField(newEntity.getPeriodType()), newEntity.getPeriod());
                    e = (Calendar) e.clone();
                    e.add(GetCalendarField(newEntity.getPeriodType()), newEntity.getPeriod());
                }
            }
        }
    }

    @Override
    public void update(final Long userID, final Map<String, Object> options, final E entity) throws UnknownIdException, PreConditionException, PermissionDeniedException {
        options.put("controller.start", System.currentTimeMillis());

        Long id = entity.getId();
        if (id == null) {
            throw missingParameter("id");
        }

        /*
         * UniverseEntity exists
         */
        E savedEntity = getFacade().retrieve(id);
        if (savedEntity == null) {
            throw new UnknownIdException(entityName, id);
        }

        /*
         * User has permissions
         */
        Boolean permission = assertPremissionsUpdateDelete(userID, savedEntity);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        if (entity instanceof AbstractEntity) {
            AbstractEntity abstractEntity = (AbstractEntity) entity;
            abstractEntity.setUpdater(new User(userID));
        }

        doUpdate(userID, options, entity);
        options.put("controller.end", System.currentTimeMillis());
    }

    public void doUpdate(final Long userID, final Map<String, Object> options, final E entity) throws PreConditionException {
        getFacade().edit(entity);
    }

    @Override
    public void delete(final Long userID, final Map<String, Object> options, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        options.put("controller.start", System.currentTimeMillis());

        if (id == null) {
            throw missingParameter("id");
        }

        /*
         * UniverseEntity exists
         */
        E savedEntity = getFacade().retrieve(id);
        if (savedEntity == null) {
            throw new UnknownIdException(entityName, id);
        }

        /*
         * User has permissions
         */
        Boolean permission = assertPremissionsUpdateDelete(userID, savedEntity);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        doDelete(userID, options, savedEntity);
        options.put("controller.end", System.currentTimeMillis());
    }

    public void doDelete(final Long userID, final Map<String, Object> options, E savedEntity) throws PreConditionException {
        getFacade().remove(savedEntity);
    }

    /*
     * Assert Permissions
     */
    public Boolean assertPremissionsCreate(final Long userID, final E entity) throws PermissionDeniedException {
        return true;
    }

    public Boolean assertPremissionsRead(final Long userID, final E entity) throws PermissionDeniedException {
        return true;
    }

    public Boolean assertPremissionsUpdateDelete(final Long userID, final E entity) throws PermissionDeniedException {
        return true;
    }

    /*
     * Getters / Setters
     */
    protected abstract Facade<E> getFacade();
}
