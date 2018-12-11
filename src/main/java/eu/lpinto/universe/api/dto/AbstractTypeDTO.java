package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Rita Portas
 */
public abstract class AbstractTypeDTO extends AbstractDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long organization;
    private Long parent;
    private Boolean enable;
    private String code;
    private String description;

    /*
     * Constructors
     */
    public AbstractTypeDTO() {
        super();
    }

    public AbstractTypeDTO(Long id) {
        super(id);
    }

    public AbstractTypeDTO(Long parent, Boolean enable, String code, String description, String name, Long creator,
            Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.parent = parent;
        this.enable = enable;
        this.code = code;
        this.description = description;
    }

    public AbstractTypeDTO(Long organization, Long parent, Boolean enable, String code, String description, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
        this.organization = organization;
        this.parent = parent;
        this.enable = enable;
        this.code = code;
        this.description = description;
    }

    /*
     * Getters and Setters
     */
    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
