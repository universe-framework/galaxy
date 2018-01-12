package eu.lpinto.universe.persistence.entities;

import javax.persistence.*;

/**
 *
 * @author lpinto
 */
@MappedSuperclass
public abstract class UniverseEntityImpl implements UniverseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public UniverseEntityImpl() {
    }

    public UniverseEntityImpl(final Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }
}
