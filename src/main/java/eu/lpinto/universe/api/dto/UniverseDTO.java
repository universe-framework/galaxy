package eu.lpinto.universe.api.dto;

import java.io.Serializable;

/**
 * UniverseDTO DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public abstract class UniverseDTO implements Serializable {

    public static final long serialVersionUID = 1L;
    private Long id;

    /*
     * Constructors
     */
    public UniverseDTO() {
        super();
    }

    public UniverseDTO(Long id) {
        this.id = id;
    }

    /*
     * Getters/Setters
     */
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
