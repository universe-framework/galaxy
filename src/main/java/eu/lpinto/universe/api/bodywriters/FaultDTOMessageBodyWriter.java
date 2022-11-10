package eu.lpinto.universe.api.bodywriters;

import eu.lpinto.universe.api.dto.FaultDTO;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.TEXT_HTML)
public class FaultDTOMessageBodyWriter implements MessageBodyWriter<FaultDTO> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return type == FaultDTO.class;
    }

    @Override
    public long getSize(FaultDTO user, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        // deprecated by JAX-RS 2.0 and ignored by Jersey runtime
        return 0;
    }

    @Override
    public void writeTo(FaultDTO fault, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream out) throws IOException, WebApplicationException {

        try (Writer writer = new PrintWriter(out)) {
            writer.write("<html>");
            writer.write("<body>");
            writer.write("<h2>Fault</h2>");
            writer.write("<p>" + fault.toString() + "</p>");
            writer.write("</body>");
            writer.write("</html>");

            writer.flush();
        }
    }
}
