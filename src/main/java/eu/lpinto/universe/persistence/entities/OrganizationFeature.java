package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 */
@Entity
@Table(name = "Organization_Feature")
public class OrganizationFeature extends AbstractEntity implements Serializable {

    static private final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    private Feature feature;

    private Float quantity;

    /*
     * Constructors
     */
    public OrganizationFeature() {
    }

    public OrganizationFeature(Long id) {
        super(id);
    }

    public OrganizationFeature(Organization organization, Feature feature, Float quantity) {
        this.organization = organization;
        this.feature = feature;
        this.quantity = quantity;
    }

    public OrganizationFeature(Organization organization, Feature feature, Float quantity, Long id, String name, User creator, Calendar created, User updater, Calendar updated, Calendar deleted) {
        super(id, name, creator, created, updater, updated, deleted);
        this.organization = organization;
        this.feature = feature;
        this.quantity = quantity;
    }

    /*
     * Getters / Setters
     */
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

}
