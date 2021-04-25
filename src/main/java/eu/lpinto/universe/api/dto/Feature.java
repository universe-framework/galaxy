package eu.lpinto.universe.api.dto;

import java.io.Serializable;

/**
 * Feature DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Feature extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    /*
     * Constructors
     */
    public Feature() {
        super();
    }

    public Feature(final Long id) {
        super(id);
    }
}
