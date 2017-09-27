package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public interface UniverseEntity extends Serializable {

    public Long getId();

    public void setId(final Long id);
}
