package com.example.assignment.common.model.enums;

import com.example.assignment.common.model.exception.CoordinationException;
import com.example.assignment.common.model.exception.ExceptionMessage;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Gender {
    ALL(0, "전체"),
    MALE(1, "남성"),
    FEMALE(2, "여성"),
    ;

    private final Integer code;

    private final String name;

    Gender(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, Gender> CODE_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(
                            Collectors.toMap(Gender::getCode, Function.identity())));

    public static Gender of(Integer code) {
        if (code == null) {
            throw new CoordinationException(ExceptionMessage.BAD_REQUEST);
        }

        Gender gender = CODE_MAP.get(code);

        if(gender != null) {
            return gender;
        }
        else {
            throw new CoordinationException(ExceptionMessage.NOT_EXIST_DATA);
        }
    }
}
