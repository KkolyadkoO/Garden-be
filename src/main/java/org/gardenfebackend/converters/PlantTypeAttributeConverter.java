package org.gardenfebackend.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.gardenfebackend.enums.PlantType;

@Converter(autoApply = true)
public class PlantTypeAttributeConverter implements AttributeConverter<PlantType, String> {

    @Override
    public String convertToDatabaseColumn(PlantType attribute) {
        if (attribute == null) {
            return null;
        }
        // Преобразуем в верхний регистр для БД
        return attribute.name().toUpperCase();
    }

    @Override
    public PlantType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            // Пробуем найти enum по точному совпадению (верхний регистр)
            return PlantType.valueOf(dbData.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            // Если не найдено, пробуем найти без учета регистра
            for (PlantType type : PlantType.values()) {
                if (type.name().equalsIgnoreCase(dbData.trim())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неизвестный тип растения в БД: " + dbData);
        }
    }
}

