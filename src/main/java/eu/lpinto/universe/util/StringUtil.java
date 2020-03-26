package eu.lpinto.universe.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author lpint
 */
public final class StringUtil {

    static public String buildString(Object[] array) {
        return buildString(array, " ");
    }

    static public String buildString(Object[] array, String separator) {
        if (array == null || array.length == 0) {
            return null;

        } else if (array.length == 1) {
            return array[0].toString();
        }

        String result = array[0].toString();

        for (int i = 1; i < array.length; i++) {
            result += separator + array[i];
        }

        return result;
    }

    /**
     * Converts a Map into a String
     *
     * @param map The source map
     * @return A string made of the concatnation of '\n\t + <code>key</code> : <code>value</code>'
     */
    static public String buildString(final Map map) {
        return buildString(map, "\n\t");
    }

    static public String buildString(final Map<Object, Object> map, String separator) {
//        return options.entrySet().stream()
//                .map((a) -> "\n\t" + a.getKey() + " : " + StringUtil.toJson(a.getValue()))
//                .reduce("", String::concat);

        if (map == null || map.isEmpty()) {
            return null;

        } else if (map.size() == 1) {
            Map.Entry<Object, Object> a = map.entrySet().iterator().next();
            return a.getKey() + " : " + StringUtil.toJson(a.getValue());
        }

        Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        Map.Entry<Object, Object> entry = iterator.next();

        StringBuilder sb = new StringBuilder(map.size() * 20);
        sb.append(entry.getKey()).append(" : ").append(StringUtil.toJson(entry.getValue()));

        while (iterator.hasNext()) {
            entry = iterator.next();
            sb.append(separator).append(entry.getKey()).append(" : ").append(StringUtil.toJson(entry.getValue()));
        }

        return sb.toString();
    }

    static public String toJson(final Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        mapper.enable(DeserializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        /*
        * Serialization
         */
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.enable(SerializationFeature.WRAP_EXCEPTIONS);
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            return mapper.writeValueAsString(obj).replace(System.lineSeparator(), System.lineSeparator() + '\t');

        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /*
     * Constructor
     */
    private StringUtil() {
        // private
    }
}
