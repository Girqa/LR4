package AdditionalClasses;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Slf4j
final public class ParsingProvider {
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private ParsingProvider() {}

    @SneakyThrows
    public static <T> void marshal(T obj, Class<T> clazz, File saveFile) {
        log.trace("Marshalling {}", obj.getClass());
        JAXBContext context = JAXBContext.newInstance(clazz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(obj, saveFile);
        log.trace("Marshalling was successful");
    }
    @SneakyThrows
    public static <T> T unmarshal(File source, Class<T> clazz) {
        log.trace("Unmarshalling {}", source.getName());
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        T result = (T) unmarshaller.unmarshal(source);
        log.trace("Unmarshalling was successful {}", source.getName());
        return result;
    }

    @SneakyThrows
    public static <T> String toJson(T obj) {
        log.trace("Converting to JSON: {}", obj.getClass().getName());
        String result = mapper.writeValueAsString(obj);
        log.trace("Successfully converted!");
        return result;
    }

    @SneakyThrows
    public static <T> T fromJson(String content, Class<T> clazz) {
        log.trace("Converting from JSON: {}", content);
        T result = mapper.readValue(content, clazz);
        log.trace("Successfully converted!");
        return result;
    }
}

