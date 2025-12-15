package org.gardenfebackend.config;

import org.gardenfebackend.enums.PlantType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlantTypeConverter implements Converter<String, PlantType> {

    @Override
    public PlantType convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        try {
            return PlantType.valueOf(source.trim());
        } catch (IllegalArgumentException e) {
            for (PlantType type : PlantType.values()) {
                if (type.name().equalsIgnoreCase(source.trim())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неизвестный тип растения: " + source);
        }
    }
}

