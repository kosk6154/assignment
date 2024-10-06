package com.example.assignment.common.model.converter;

import com.example.assignment.common.model.enums.ClothType;
import jakarta.persistence.AttributeConverter;

public class ClothTypeConverter implements AttributeConverter<ClothType, String> {

    @Override
    public String convertToDatabaseColumn(ClothType attribute) {
        return attribute.getCode();
    }

    @Override
    public ClothType convertToEntityAttribute(String dbData) {
        return ClothType.of(dbData);
    }
}
