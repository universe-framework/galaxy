package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Image;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class AvatarsDTS {

    public static List<String> urls(final List<Image> images) {
        return images == null ? null
               : images.parallelStream().map(image -> image.getUrl()).collect(Collectors.toList());
    }
}
