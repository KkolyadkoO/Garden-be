package org.gardenfebackend.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.gardenfebackend.enums.GardenType;

@Converter(autoApply = true)
public class GardenTypeAttributeConverter implements AttributeConverter<GardenType, String> {

    @Override
    public String convertToDatabaseColumn(GardenType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name().toUpperCase();
    }

    @Override
    public GardenType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return GardenType.valueOf(dbData.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            for (GardenType type : GardenType.values()) {
                if (type.name().equalsIgnoreCase(dbData.trim())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неизвестный тип огорода в БД: " + dbData);
        }
    }
}

