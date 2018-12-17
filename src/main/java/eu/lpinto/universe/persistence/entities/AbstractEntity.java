package eu.lpinto.universe.persistence.entities;

import java.util.Calendar;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Abstract base for all entities.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@MappedSuperclass
public abstract class AbstractEntity implements UniverseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    @Size(min = 1, message = "Invalid name size")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Calendar created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private User updater;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Calendar updated;

    @Transient
    private boolean full = false;

    /*
     * Public helpers
     */
    static public String buildCode(String part1, String part2) {
        return part1 == null || part2 == null ? null
               : part1.replaceAll("\\s+", "_").toLowerCase() + "-" + part2.replaceAll("\\s+", "_").toLowerCase();
    }

    /*
     * Constructors
     */
    public AbstractEntity() {
    }

    public AbstractEntity(final Long id) {
        this.id = id;
    }

    public AbstractEntity(final String name) {
        this.name = name;
    }

    public AbstractEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public AbstractEntity(final Long id, final String name, final Calendar created, final Calendar updated) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.updated = updated;
    }

    public AbstractEntity(String name, User creator, Calendar created, User updater, Calendar updated, Long id) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.created = created;
        this.updater = updater;
        this.updated = updated;
    }

    /*
     * Overide
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object other) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(other instanceof AbstractEntity)) {
            return false;
        }

        return this.getId() != null && this.getId().equals(((UniverseEntity) other).getId());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [id=" + getId() + "]";
    }

    /*
     * Getters / Setters
     */
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        assertNotNull(name);

        this.name = name;
    }

    public void setName(final Object obj1, final Object obj2) {
        String str1 = obj1 == null ? this.getClass().getSimpleName() : obj1.toString();
        String str2 = obj2 == null ? this.getClass().getSimpleName() : obj2.toString();

        String newName = buildCode(str1, str2);
        assertNotNull(newName);

        this.name = newName;
    }

    public Calendar getCreated() {
        return this.created;
    }

    public void setCreated(final Calendar created) {
        assertNotNull(created);

        this.created = created;
    }

    public Calendar getUpdated() {
        return updated;
    }

    public void setUpdated(final Calendar updated) {
        assertNotNull(updated);

        this.updated = updated;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(final boolean full) {
        this.full = full;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(final User creator) {
        this.creator = creator;
    }

    public User getUpdater() {
        return updater;
    }

    public void setUpdater(final User updater) {
        this.updater = updater;
    }

    /*
     * Protected helpers
     */
    protected void assertNotNull(final List<Object> field) {
        if (field == null || field.isEmpty()) {
            throw new IllegalArgumentException("Cannot set field with null or empty List");
        }
    }

    protected void assertNotNull(final String field) {
        if (field == null || field.isEmpty()) {
            throw new IllegalArgumentException("Cannot set field with null or empty String");
        }
    }

    protected void assertNotNull(final Object field) {
        if (field == null) {
            throw new IllegalArgumentException("Cannot set field with null");
        }
    }
}
