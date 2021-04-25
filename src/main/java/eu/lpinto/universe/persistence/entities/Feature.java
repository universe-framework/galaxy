package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "Feature")
public class Feature extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /*
     * Contructors
     */
    public Feature() {
    }

    public Feature(Long id) {
        super(id);
    }

    public Feature(final Long id, final String name, final Calendar created, final Calendar updated) {
        super(id, name, created, updated);
    }
}
