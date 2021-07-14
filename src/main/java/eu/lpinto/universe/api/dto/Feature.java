package eu.lpinto.universe.api.dto;

import java.io.Serializable;

/**
 * Feature DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Feature extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    private Integer office;

    /*
     * Constructors
     */
    public Feature() {
        super();
    }

    public Feature(final Long id) {
        super(id);
    }

    public Feature(Integer office) {
        this.office = office;
    }

    public Feature(Integer office, String name, Long id) {
        super(name, id);
        this.office = office;
    }

    public Integer getOffice() {
        return office;
    }

    public void setOffice(Integer office) {
        this.office = office;
    }
}
