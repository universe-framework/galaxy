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

    /*
     * Constructors
     */
    public OrganizationFeature() {
    }

    public OrganizationFeature(Long id) {
        super(id);
    }

    public OrganizationFeature(Organization organization, Feature feature) {
        this.organization = organization;
        this.feature = feature;
    }

    public OrganizationFeature(Organization organization, Feature feature, Long id, String name, User creator, Calendar created, User updater, Calendar updated, Calendar deleted) {
        super(id, name, creator, created, updater, updated, deleted);
        this.organization = organization;
        this.feature = feature;
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
}
