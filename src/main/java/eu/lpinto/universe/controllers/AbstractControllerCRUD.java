package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.AbstractEntity;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.Facade;
import java.util.List;
import java.util.Map;

/**
 * Foundation controller for CRUD operations
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 * @param <E> Domain UniverseEntity
 */
public abstract class AbstractControllerCRUD<E extends UniverseEntity> extends AbstractController implements CrudController<E> {

    private final String entityName;

    /*
     * Constructors
     */
    public AbstractControllerCRUD(final String entityName) {
        super();
        this.entityName = entityName;
    }

    /*
     * Services
     */
    @Override
    public List<E> find(final Long userID, final Map<String, Object> options) throws PermissionDeniedException, PreConditionException {
        Boolean permission = assertPremissionsRead(userID, null);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        return doFind(userID, options);
    }

    public List<E> doFind(final Long userID, final Map<String, Object> options) throws PreConditionException {
        return getFacade().find(options);
    }

    @Override
    public E retrieve(final Long userID, final Map<String, Object> options, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        /*
         * Preconditions
         */
        if (id == null) {
            throw missingParameter("id");
        }

        /*
         * Body
         */
        E savedEntity;
        savedEntity = doRetrieve(userID, options, id);

        if (savedEntity == null) {
            throw new UnknownIdException(entityName, id);
        }

        Boolean permission = assertPremissionsRead(userID, savedEntity);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        return savedEntity;
    }

    public E doRetrieve(final Long userID, final Map<String, Object> options, final Long id) throws UnknownIdException, PreConditionException {
        E savedEntity;
        savedEntity = getFacade().retrieve(id);
        return savedEntity;
    }

    @Override
    public E create(final Long userID, final Map<String, Object> options, final E entity) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        Boolean permission = assertPremissionsCreate(userID, entity);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        if (userID != null && entity instanceof AbstractEntity) {
            AbstractEntity abstractEntity = (AbstractEntity) entity;
            abstractEntity.setCreator(new User(userID));
        }

        doCreate(userID, options, entity);
        return entity;
    }

    public void doCreate(final Long userID, final Map<String, Object> options, final E entity) throws UnknownIdException, PermissionDeniedException, PreConditionException {
        getFacade().create(entity);
    }

    @Override
    public void update(final Long userID, final Map<String, Object> options, final E entity) throws UnknownIdException, PreConditionException, PermissionDeniedException {
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
    }

    public void doUpdate(final Long userID, final Map<String, Object> options, final E entity) throws PreConditionException {
        getFacade().edit(entity);
    }

    @Override
    public void delete(final Long userID, final Map<String, Object> options, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
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
    }

    public void doDelete(final Long userID, final Map<String, Object> options, E savedEntity) throws PreConditionException {
        getFacade().remove(savedEntity);
    }

    /*
     * Protected
     */
    protected abstract Facade<E> getFacade();

    public Boolean assertPremissionsCreate(final Long userID, final E entity) throws PermissionDeniedException {
        return true;
    }

    public Boolean assertPremissionsRead(final Long userID, final E entity) throws PermissionDeniedException {
        return true;
    }

    public Boolean assertPremissionsUpdateDelete(final Long userID, final E entity) throws PermissionDeniedException {
        return true;
    }
}
