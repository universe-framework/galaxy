package eu.lpinto.universe.api.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.lpinto.universe.util.UniverseProperties;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Configures the behaviour of Jackson library.
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper = new ObjectMapper();

    public JacksonConfigurator() {
        /* Deserialization */
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        /* Serialization */
        mapper.enable(SerializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        /* Other */
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        mapper.registerModule(new JavaTimeModule());

        /* DEV */
        if (UniverseProperties.VERSION.indexOf("SNAPSHOT") > 1) {
            mapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);

        }
    }

    @Override
    public ObjectMapper getContext(Class<?> clazz) {
        return mapper;
    }
}
