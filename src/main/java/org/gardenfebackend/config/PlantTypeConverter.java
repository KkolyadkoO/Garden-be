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
            // Пробуем найти enum по точному совпадению
            return PlantType.valueOf(source.trim());
        } catch (IllegalArgumentException e) {
            // Если не найдено, пробуем найти без учета регистра
            for (PlantType type : PlantType.values()) {
                if (type.name().equalsIgnoreCase(source.trim())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неизвестный тип растения: " + source);
        }
    }
}

