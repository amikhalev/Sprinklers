package org.amikhalev.sprinklers.converters;

import com.google.gson.Gson;
import org.amikhalev.sprinklers.AppConfig;
import org.amikhalev.sprinklers.service.Section;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by alex on 4/29/15.
 */
@Converter
public class SectionConverter implements AttributeConverter<Section, String> {
    private Gson gson = AppConfig.gson();

    @Override
    public String convertToDatabaseColumn(Section attribute) {
        return gson.toJson(attribute, Section.class);
    }

    @Override
    public Section convertToEntityAttribute(String dbData) {
        return gson.fromJson(dbData, Section.class);
    }
}
