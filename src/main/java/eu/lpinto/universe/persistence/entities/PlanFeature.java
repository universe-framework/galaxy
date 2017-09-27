package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "Plan_Feature")
public class PlanFeature extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE) // cascade merge will update target object with this instance
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "feature_id")
    private Feature feature;

    /*
     * Constructors
     */
    public PlanFeature() {
        super();
    }

    public PlanFeature(final Long id) {
        super(id);
    }

    public PlanFeature(final Integer value, final Plan plan, final Feature feature,
                       final Long id, final String name, final Calendar created, final Calendar updated) {
        super(id, name, created, updated);
        this.value = value;
        this.plan = plan;
        this.feature = feature;
    }

    /*
     * Getters/Setters
     */
    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(final Plan plan) {
        this.plan = plan;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(final Feature feature) {
        this.feature = feature;
    }
}
