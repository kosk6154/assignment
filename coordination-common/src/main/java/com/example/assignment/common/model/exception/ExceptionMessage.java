package com.example.assignment.common.model.exception;

import lombok.Getter;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 하나씩 exception을 생성하는게 맞지만 과제 개발 편의를 위해 한곳에 메시지들을 모아서 관리한다.
 */
@Getter
public enum ExceptionMessage {
    UNKNOWN(1000, "Unknown", HttpURLConnection.HTTP_INTERNAL_ERROR),
    BAD_REQUEST(1001, "Bad request.", HttpURLConnection.HTTP_BAD_REQUEST),
    INVALID_PARAMETER(1002, "Invalid parameter.", HttpURLConnection.HTTP_BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1003, "Internal server error.", HttpURLConnection.HTTP_BAD_REQUEST),
    ALREADY_EXIST_DATA(2000, "Already exist data.", HttpURLConnection.HTTP_BAD_REQUEST),
    NOT_EXIST_DATA(2001, "Not exist data.", HttpURLConnection.HTTP_NOT_FOUND),
    SALE_CLOTH_EXIST(2002, "There is clothing on sale from the brand.", HttpURLConnection.HTTP_BAD_REQUEST),
    NOT_EXIST_BRAND(2003, "There is no such brand.", HttpURLConnection.HTTP_BAD_REQUEST),
    ;

    private final Integer code;

    private final String message;

    private final int status;

    private static final Map<Integer, ExceptionMessage> CODE_MAP =
            Collections.unmodifiableMap(
                    Stream.of(values()).collect(
                            Collectors.toMap(ExceptionMessage::getCode, Function.identity())));

    ExceptionMessage(Integer code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public static ExceptionMessage of(int code) {
        return CODE_MAP.get(code);
    }
}
