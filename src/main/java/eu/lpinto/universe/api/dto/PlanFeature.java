package eu.lpinto.universe.api.dto;

/**
 * Represents the relation between an feature and a owner.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class PlanFeature extends AbstractDTO {

    private static final long serialVersionUID = 1L;

    private Integer value;
    private Long plan;
    private Long feature;

    /*
     * Constructors
     */
    public PlanFeature() {
        super();
    }

    public PlanFeature(Long id) {
        super(id);
    }

    public PlanFeature(Integer value, Long plan, Long feature) {
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

    public void setValue(Integer value) {
        this.value = value;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(final Long feature) {
        this.feature = feature;
    }

    public Long getPlan() {
        return plan;
    }

    public void setPlan(final Long plan) {
        this.plan = plan;
    }
}
