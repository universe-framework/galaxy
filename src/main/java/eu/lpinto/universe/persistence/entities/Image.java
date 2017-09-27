package eu.lpinto.universe.persistence.entities;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Image Entity
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Entity
@Table(name = "Image")
public class Image extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(length = 2048)
    private String url;

    /*
     * Contructors
     */
    public Image() {
    }

    public Image(Long id) {
        super(id);
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
