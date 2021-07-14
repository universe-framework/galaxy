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

    private Integer office;

    /*
     * Contructors
     */
    public Feature() {
    }

    public Feature(Long id) {
        super(id);
    }

    public Feature(Integer office) {
        this.office = office;
    }

    public Feature(final Long id, final String name, final Calendar created, final Calendar updated) {
        super(id, name, created, updated);
    }

    public Integer getOffice() {
        return office;
    }

    public void setOffice(Integer office) {
        this.office = office;
    }
}
