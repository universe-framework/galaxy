package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.api.util.Digest;
import eu.lpinto.universe.persistence.entities.Image;
import eu.lpinto.universe.persistence.entities.User;

/**
 * User DTO - Data Transformation Object
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class UserDTS extends AbstractDTS<User, eu.lpinto.universe.api.dto.User> {

    public static final UserDTS T = new UserDTS();

    @Override
    public eu.lpinto.universe.api.dto.User buildDTO(User entity) {
        return new eu.lpinto.universe.api.dto.User(
                entity.getCurrentAvatar() == null ? null : entity.getCurrentAvatar().getUrl(),
                entity.getEmail(),
                null, // passowrd is never released
                entity.getName(),
                AbstractDTS.toApiID(entity.getCreator()),
                entity.getCreated(),
                AbstractDTS.toApiID(entity.getUpdater()),
                entity.getUpdated(),
                entity.getId());
    }

    @Override
    public User toDomain(Long id) {
        if (id == null) {
            return null;
        }

        return new User(id);
    }

    public User toDomain(final String email, final String password) {
        return new User(email, password);
    }

    public User toDomain(final String email, final String password, final String name) {
        return new User(email, password, name);
    }

    @Override
    public User buildEntity(eu.lpinto.universe.api.dto.User dto) {
        return new User(dto.getBaseUrl(),
                        dto.getCurrentAvatar() == null ? null : new Image(dto.getCurrentAvatar()),
                        null, // avatars
                        null, // tokens
                        null, // employees
                        dto.getEmail(),
                        Digest.getSHA(dto.getPassword()),
                        dto.getName(),
                        UserDTS.T.toDomain(dto.getCreator()),
                        dto.getCreated(),
                        UserDTS.T.toDomain(dto.getUpdater()),
                        dto.getUpdated(),
                        dto.getId(),
                        dto.getDeleted());
    }
}
