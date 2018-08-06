package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Image Entity
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "Image")
public class Image extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(nullable = false,
            unique = true, // sed only in DDL generation
            updatable = false,
            length = 255)
    @Size(min = 1, max = 255, message = "Invalid email size")
    private String url;

    /*
     * Contructors
     */
    public Image() {
    }

    public Image(Long id) {
        super(id);
    }

    public Image(String url) {
        this.url = url;
    }

    public Image(final String url, final Long id, final String name, final Calendar created, final Calendar updated) {
        super(id, name, created, updated);
        this.url = url;
    }

    /*
     * Getters/Setters
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
