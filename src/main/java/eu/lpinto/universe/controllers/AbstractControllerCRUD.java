package eu.lpinto.universe.controllers;

import eu.lpinto.universe.controllers.exceptions.PermissionDeniedException;
import eu.lpinto.universe.controllers.exceptions.PreConditionException;
import eu.lpinto.universe.controllers.exceptions.UnknownIdException;
import eu.lpinto.universe.persistence.entities.AbstractEntity;
import eu.lpinto.universe.persistence.entities.UniverseEntity;
import eu.lpinto.universe.persistence.entities.User;
import eu.lpinto.universe.persistence.facades.Facade;
import java.util.HashMap;
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
    public List<E> doFindAll() throws PermissionDeniedException {
        return getFacade().findAll();
    }

    @Override
    public final List<E> find(final Long userID, final Map<String, Object> options) throws PermissionDeniedException, PreConditionException {
        Boolean permission = assertPremissionsRead(userID, null);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        try {
            return doFind(userID, options);

        }
        catch (RuntimeException ex) {
            throw internalError(ex);
        }
    }

    public List<E> doFind(final Long userID, final Map<String, Object> options) throws PreConditionException {
        return getFacade().find(options);
    }

    @Override
    public final E retrieve(final Long userID, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
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
        try {
            savedEntity = doRetrieve(id);

            if (savedEntity == null) {
                throw new UnknownIdException(entityName, id);
            }
        }
        catch (RuntimeException ex) {
            throw internalError(ex);
        }

        Boolean permission = assertPremissionsRead(userID, savedEntity);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        return savedEntity;
    }

    public E doRetrieve(final Long id) throws UnknownIdException, PreConditionException {
        E savedEntity;
        savedEntity = getFacade().retrieve(id);
        return savedEntity;
    }

    @Override
    public final E create(final Long userID, final E entity) throws PreConditionException, PermissionDeniedException {
        Boolean permission = assertPremissionsCreate(userID, entity);
        if (false == isSystemAdmin(userID) && (permission == null || false == permission)) {
            throw new PermissionDeniedException();
        }

        if (entity instanceof AbstractEntity) {
            AbstractEntity abstractEntity = (AbstractEntity) entity;
            abstractEntity.setCreator(new User(userID));
        }

        Map<String, Object> options = new HashMap<>(1);
        options.put("user", userID);

        try {
            doCreate(entity, options);
            return entity;
        }
        catch (RuntimeException ex) {
            throw internalError(ex);
        }
    }

    public void doCreate(final E entity, final Map<String, Object> options) throws PreConditionException {
        getFacade().create(entity);
    }

    @Override
    public final void update(final Long userID, final E entity) throws UnknownIdException, PreConditionException, PermissionDeniedException {
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

        try {
            doUpdate(entity);
        }
        catch (RuntimeException ex) {

            throw internalError(ex);
        }
    }

    public void doUpdate(final E entity) {
        getFacade().edit(entity);
    }

    @Override
    public final void delete(final Long userID, final Long id) throws UnknownIdException, PermissionDeniedException, PreConditionException {
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

        try {
            doDelete(savedEntity);
        }
        catch (RuntimeException ex) {

            throw internalError(ex);
        }
    }

    public void doDelete(E savedEntity) throws PreConditionException {
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
