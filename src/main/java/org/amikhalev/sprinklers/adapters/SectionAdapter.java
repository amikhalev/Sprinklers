package org.amikhalev.sprinklers.adapters;

import com.google.gson.*;
import org.amikhalev.sprinklers.service.Section;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Type;

/**
 * Created by alex on 4/29/15.
 */
public class SectionAdapter implements JsonSerializer<Section>, JsonDeserializer<Section> {
    public static final String KEY_CLASS = "class";
    public static final String KEY_DATA = "data";
    private static final Log log = LogFactory.getLog(SectionAdapter.class);

    @Override
    public Section deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        log.debug("deserializing json '" + json + "'");
        JsonObject object = json.getAsJsonObject();
        String className = object.getAsJsonPrimitive(KEY_CLASS).getAsString();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Cannot find section class '" + className + "'", e);
        }
        JsonElement data = object.get(KEY_DATA);
        return context.deserialize(data, clazz);
    }

    @Override
    public JsonElement serialize(Section src, Type typeOfSrc, JsonSerializationContext context) {
        log.debug("serializing section " + src);
        JsonObject object = new JsonObject();
        object.addProperty(KEY_CLASS, src.getClass().getCanonicalName());
        object.add(KEY_DATA, context.serialize(src));
        log.debug("resulting object: " + object);
        return object;
    }
}
