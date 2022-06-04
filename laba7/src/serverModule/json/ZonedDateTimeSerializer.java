package serverModule.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;

import static common.utils.DateConverter.zonedDateTimeToString;

public class ZonedDateTimeSerializer implements JsonSerializer <ZonedDateTime> {
    public JsonElement serialize(ZonedDateTime zonedDateTime, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(zonedDateTimeToString(zonedDateTime));
    }
}
