package eu.lpinto.universe.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private String migrationCode;
    private Long migrationId;

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

    @JsonSetter
    public void setCreated(Calendar created) {
        this.created = created == null ? null : (Calendar) created.clone();
    }

    @JsonIgnore
    public void setCreated(final LocalDate localDate) {
        if (localDate == null) {
            return;
        }

        Calendar aux = new GregorianCalendar();
        aux.setTime(java.sql.Date.valueOf(localDate));
        this.created = aux;
    }

    @JsonIgnore
    public void setCreated(final Instant instant) {
        if (instant == null) {
            return;
        }

        Calendar aux = new GregorianCalendar();
        aux.setTime(Date.from(instant));
        this.created = aux;
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

    @JsonSetter
    public void setUpdated(Calendar updated) {
        this.updated = updated == null ? null : (Calendar) updated.clone();
    }

    @JsonIgnore
    public void setUpdated(final LocalDate localDate) {
        if (localDate == null) {
            return;
        }

        Calendar aux = new GregorianCalendar();
        aux.setTime(java.sql.Date.valueOf(localDate));
        this.updated = aux;
    }

    @JsonIgnore
    public void setUpdated(final Instant instant) {
        if (instant == null) {
            return;
        }

        Calendar aux = new GregorianCalendar();
        aux.setTime(Date.from(instant));
        this.updated = aux;
    }

    public Calendar getDeleted() {
        return deleted;
    }

    @JsonSetter
    public void setDeleted(Calendar deleted) {
        this.deleted = deleted == null ? null : (Calendar) deleted.clone();
    }

    @JsonIgnore
    public void setDeleted(final LocalDate localDate) {
        if (localDate == null) {
            return;
        }

        Calendar aux = new GregorianCalendar();
        aux.setTime(java.sql.Date.valueOf(localDate));
        this.deleted = aux;
    }

    @JsonIgnore
    public void setDeleted(final Instant instant) {
        if (instant == null) {
            return;
        }

        Calendar aux = new GregorianCalendar();
        aux.setTime(Date.from(instant));
        this.deleted = aux;
    }

    public String getMigrationCode() {
        return migrationCode;
    }

    public void setMigrationCode(String migrationCode) {
        this.migrationCode = migrationCode;
    }

    public Long getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Long migrationId) {
        this.migrationId = migrationId;
    }
}
