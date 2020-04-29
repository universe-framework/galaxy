package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "Feature")
public class Feature extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "feature", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<PlanFeature> plans;

    /*
     * Contructors
     */
    public Feature() {
    }

    public Feature(Long id) {
        super(id);
    }

    public Feature(List<PlanFeature> plans,
                   final Long id, final String name, final Calendar created, final Calendar updated) {
        super(id, name, created, updated);
        this.plans = plans;
    }

    /*
     * Getters/Setters
     */
    public List<PlanFeature> getPlans() {
        return plans;
    }

    public void setPlans(List<PlanFeature> plans) {
        this.plans = plans;
    }
}
