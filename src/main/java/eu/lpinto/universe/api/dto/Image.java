package eu.lpinto.universe.api.dto;

import java.io.Serializable;

/**
 * Image DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Image extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long organization;
    private String url;

    /*
     * Constructors
     */
    public Image() {
        super();
    }

    public Image(final Long id) {
        super(id);
    }

    public Image(String url) {
        this.url = url;
    }

    public Image(Long organization, String url) {
        this.organization = organization;
        this.url = url;
    }

    /*
     * Getters/Setters
     */
    public Long getOrganization() {
        return organization;
    }

    public void setOrganization(Long organization) {
        this.organization = organization;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
