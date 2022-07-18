package eu.lpinto.universe.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    static public String buildString(final Calendar calendar) {
        return buildString(calendar, TimeZone.getTimeZone("Europe/Lisbon"));
    }

    static public String buildString(final Calendar calendar, TimeZone tz) {
        Calendar aux = (Calendar) calendar.clone();
        aux.setTimeZone(tz);

        return "" + aux.get(Calendar.YEAR)
               + "-" + String.format("%02d", aux.get(Calendar.MONTH) + 1)
               + "-" + String.format("%02d", aux.get(Calendar.DAY_OF_MONTH));
    }

    /*
     * To a spacific format
     */
    static public String toAscii(final String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    static public String toJson(final Object obj) {
        ObjectMapper mapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        mapper.registerModule(javaTimeModule);
        mapper.registerModule(javaTimeModule);

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

    static public String buildStringDate(final Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    static public String buildStringTime(final Calendar calendar) {
        return buildString(calendar, TimeZone.getTimeZone("Europe/Lisbon"));
    }

    static public String buildStringTime(final Calendar calendar, TimeZone tz) {
        Calendar aux = (Calendar) calendar.clone();
        aux.setTimeZone(tz);

        return "" + aux.get(Calendar.HOUR_OF_DAY) + ":" + aux.get(Calendar.MINUTE);
    }

    /*
     * Constructor
     */
    private StringUtil() {
        // private
    }

    /*
     * Other classes
     */
    static private class LocalDateSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ISO_DATE));
        }
    }

    static private class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ISO_DATE_TIME);
        }
    }
}
