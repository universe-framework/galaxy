package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author Luis Pinto <code>- luis.pinto@petuniversal.com</code>
 * @param <T> Some class extending this one.
 */
@MappedSuperclass
public abstract class AbstractType<T extends AbstractType<T>> extends AbstractEntity implements Serializable {

    static private final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Organization organization;
    @Column(columnDefinition = "boolean DEFAULT 1")
    private Boolean enable = true;
    private String code;
    private String description;

    /*
     * Constructors
     */
    public AbstractType() {
        super();
    }

    public AbstractType(Long id) {
        super(id);
    }

    public AbstractType(String name) {
        super(name);
    }

    public AbstractType(Long id, String name) {
        super(id, name);
    }

    public AbstractType(String code, Boolean enable, String name) {
        super(name);
        setCode(code);
        setEnable(enable);
    }

    public AbstractType(String code, String description, String name) {
        super(name);
        setCode(code);
        setDescription(description);
    }

    public AbstractType(Organization organization, String code, Boolean enable, String name,
            User creator, Calendar created, User updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.organization = organization;
        setCode(code);
        this.enable = enable;
    }

    public AbstractType(Boolean enable, String code, String description,
            String name, User creator, Calendar created, User updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.enable = enable;
        setCode(code);
        this.description = description;
    }

    public AbstractType(Organization organization, Boolean enable, String code, String description,
            String name, User creator, Calendar created, User updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.organization = organization;
        this.enable = enable;
        setCode(code);
        this.description = description;
    }

    /*
     * Getters/Setters
     */
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization species) {
        this.organization = species;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code.replaceAll(" ", "_").toLowerCase();
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public abstract T getParent();

    public abstract void setParent(T parent);

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
