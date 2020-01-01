package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Token;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Token DTS - Data Transformation Service
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class TokenDTS {

    public static final TokenDTS T = new TokenDTS();

    public List<String> ids(final List<Token> entities) {
        return entities == null || entities.isEmpty() ? null
               : entities.parallelStream().map(entity -> entity.getToken()).collect(Collectors.toList());
    }

    public Token toDomain(String token) {
        if (token == null) {
            return null;
        }

        return new Token(token);
    }

    public List<Token> toDomainIDs(final List<String> dtoIDs) {
        return dtoIDs == null || dtoIDs.isEmpty() ? null
               : dtoIDs.parallelStream().map(dto -> toDomain(dto)).collect(Collectors.toList());
    }

    public eu.lpinto.universe.api.dto.Token toAPI(Token entity) {
        return new eu.lpinto.universe.api.dto.Token(entity.getToken());
    }
}
