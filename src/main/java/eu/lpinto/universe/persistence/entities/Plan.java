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
@Table(name = "Plan")
public class Plan extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "plan", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<PlanFeature> features;

    /*
     * Contructors
     */
    public Plan() {
    }

    public Plan(Long id) {
        super(id);
    }

    public Plan(List<PlanFeature> features,
                final Long id, final String name, final Calendar created, final Calendar updated) {
        super(id, name, created, updated);
        this.features = features;
    }

    /*
     * Getters/Setters
     */
    public List<PlanFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<PlanFeature> features) {
        this.features = features;
    }
}
