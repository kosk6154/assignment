package com.example.assignment.common.model.enums;

import com.example.assignment.common.model.exception.CoordinationException;
import com.example.assignment.common.model.exception.ExceptionMessage;
import lombok.Getter;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ClothType {
    TOP("top", "상의"),
    BOTTOM("bottom", "하의"),
    SHOES("shoes", "신발"),
    BAG("bag", "가방"),
    HAT("hat", "모자"),
    SOCKS("socks", "양말"),
    ACCESSORY("accessory", "액세서리"),
    OUTER("outer", "아우터"),
    SNEAKERS("sneakers", "스니커즈"),
    PANTS("pants", "바지"),
    ;

    private final String code;

    private final String name;

    ClothType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, ClothType> CODE_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(
                            Collectors.toMap(ClothType::getCode, Function.identity())));

    public static ClothType of(String code) {
        if (code == null) {
            throw new CoordinationException(ExceptionMessage.BAD_REQUEST);
        }

        ClothType clothType = CODE_MAP.get(code.toLowerCase(Locale.ROOT));

        if(clothType != null) {
            return clothType;
        }
        else {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_DATA);
        }
    }
}

