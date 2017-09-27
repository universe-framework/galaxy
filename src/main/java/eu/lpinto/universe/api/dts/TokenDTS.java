package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Token;
import java.util.ArrayList;
import java.util.List;

/**
 * Token DTS - Data Transformation Service
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class TokenDTS {

    public static final TokenDTS T = new TokenDTS();

    public List<String> ids(final List<Token> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }

        List<String> result = new ArrayList<>(entities.size());

        for (Token entity : entities) {
            result.add(entity.getToken());
        }

        return result;
    }

    public Token toDomain(String token) {
        if (token == null) {
            return null;
        }

        return new Token(token);
    }

    public List<Token> toDomainIDs(final List<String> dtoIDs) {
        if (dtoIDs == null) {
            return null;
        }

        List<Token> result = new ArrayList<>(dtoIDs.size());

        for (String token : dtoIDs) {
            result.add(toDomain(token));
        }

        return result;
    }

    public eu.lpinto.universe.api.dto.Token toAPI(Token entity) {
        return new eu.lpinto.universe.api.dto.Token(entity.getToken());
    }
}
