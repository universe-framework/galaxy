package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Base entity, encapsulates the common data between all entities.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public abstract class AbstractDTO extends UniverseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Long creator;
    private Calendar created;
    private Long updater;
    private Calendar updated;
    private Calendar deleted;

    /*
     * Constructors
     */
    protected AbstractDTO() {
    }

    protected AbstractDTO(final Long id) {
        super(id);
    }

    public AbstractDTO(String name) {
        this.name = name;
    }

    public AbstractDTO(String name, Long id) {
        super(id);
        this.name = name;
    }

    public AbstractDTO(String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(id);
        this.name = name;
        this.creator = creator;
        this.created = created;
        this.updater = updater;
        this.updated = updated;
    }

    public AbstractDTO(Long id, String name, Long creator, Calendar created, Long updater, Calendar updated, Calendar deleted) {
        super(id);
        this.name = name;
        this.creator = creator;
        this.created = created;
        this.updater = updater;
        this.updated = updated;
        this.deleted = deleted;
    }

    /*
     * Getters/Setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(Calendar updated) {
        this.updated = updated;
    }

    public Calendar getDeleted() {
        return deleted;
    }

    public void setDeleted(Calendar deleted) {
        this.deleted = deleted;
    }
}
