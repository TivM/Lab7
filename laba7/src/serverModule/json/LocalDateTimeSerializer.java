package serverModule.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import static common.utils.DateConverter.localDateTimeToString;

/**
 * type adapter for json serialization
 */
public class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime>{
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTimeToString(localDateTime));
    }
}
