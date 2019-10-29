package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Image;

/**
 * Image DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class ImageDTS extends AbstractDTS<Image, eu.lpinto.universe.api.dto.Image> {

    public static final ImageDTS T = new ImageDTS();

    @Override
    public eu.lpinto.universe.api.dto.Image toAPI(Image entity) {
        return new eu.lpinto.universe.api.dto.Image(
                entity.getUrl(),
                entity.getName(),
                AbstractDTS.id(entity.getCreator()),
                entity.getCreated(),
                AbstractDTS.id(entity.getUpdater()),
                entity.getUpdated(),
                entity.getId());
    }

    @Override
    public Image toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new Image(id);
    }

    @Override
    public Image toDomain(eu.lpinto.universe.api.dto.Image dto) {
        return new Image(
                dto.getUrl(),
                dto.getId(), dto.getName(), dto.getCreated(), dto.getUpdated());
    }
}
