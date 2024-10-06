package com.example.assignment.coordinationapi.application.model.converter;

import com.example.assignment.common.model.enums.ClothType;
import org.springframework.core.convert.converter.Converter;

public class StringToClothTypeConverter implements Converter<String, ClothType> {

    @Override
    public ClothType convert(String source) {
        return ClothType.of(source);
    }
}
