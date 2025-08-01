package eu.lpinto.universe.api.dto;

import java.io.Serializable;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
public class OrganizationFeatureDTO extends AbstractDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long organization;
    private Long feature;
    private Float quantity;

    public OrganizationFeatureDTO() {
    }

    public OrganizationFeatureDTO(Long id) {
        super(id);
    }

    public OrganizationFeatureDTO(Long organization, Long feature, Float quantity) {
        this.organization = organization;
        this.feature = feature;
        this.quantity = quantity;
    }

    public OrganizationFeatureDTO(Long organization, Long feature, Float quantity, String name, Long id) {
        super(name, id);
        this.organization = organization;
        this.feature = feature;
        this.quantity = quantity;
    }

    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public Long getFeature() {
        return feature;
    }

    public void setFeature(Long feature) {
        this.feature = feature;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

}
