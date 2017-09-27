package eu.lpinto.universe.api.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Image DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class Image extends AbstractDTO implements Serializable {

    public static final long serialVersionUID = 1L;

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

    public Image(String url, String name, Long creator, Calendar created, Long updater, Calendar updated, Long id) {
        super(name, creator, created, updater, updated, id);
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
