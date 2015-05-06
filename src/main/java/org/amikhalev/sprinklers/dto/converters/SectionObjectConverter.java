package org.amikhalev.sprinklers.dto.converters;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.amikhalev.sprinklers.service.Section;
import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by alex on 5/5/15.
 */
public class SectionObjectConverter implements CustomConverter {
    @Autowired
    private Gson gson;

    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if (sourceFieldValue == null)
            return null;
        if (sourceFieldValue instanceof Section) {
            Section section = (Section) sourceFieldValue;
            return gson.toJson(section);
        } else if (sourceFieldValue instanceof String) {
            String data = (String) sourceFieldValue;
            try {
                return gson.fromJson(data, Section.class);
            } catch (JsonSyntaxException e) {
                throw new MappingException("Error deserializing section", e);
            }
        } else {
            throw new MappingException("SectionObjectConverter used incorrectly. " + sourceFieldValue + " -> " + existingDestinationFieldValue);
        }
    }
}
