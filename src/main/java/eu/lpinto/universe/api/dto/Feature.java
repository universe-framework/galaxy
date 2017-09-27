package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Feature DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Feature extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    private List<Long> plans;

    /*
     * Constructors
     */
    public Feature() {
        super();
    }

    public Feature(final Long id) {
        super(id);
    }

    public Feature(List<Long> plans, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.plans = plans;
    }

    /*
     * Getters/Setters
     */
    public List<Long> getPlans() {
        return plans;
    }

    public void setPlans(final List<Long> plans) {
        this.plans = plans;
    }
}
