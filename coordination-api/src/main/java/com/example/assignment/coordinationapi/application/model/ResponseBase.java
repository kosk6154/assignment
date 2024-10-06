package com.example.assignment.coordinationapi.application.model;

import com.example.assignment.common.model.exception.CoordinationException;
import com.example.assignment.common.model.exception.ExceptionMessage;
import lombok.Getter;
import org.springdoc.api.ErrorMessage;

@Getter
public class ResponseBase<T> {
    private boolean success;
    private ExceptionBase exception;
    private T payload;

    public static <T> ResponseBase<T> of(boolean success, Exception exception, T payload){
        ResponseBase<T> result = new ResponseBase<>();

        result.success = success;

        if(exception instanceof CoordinationException cx) {
            result.exception = new ExceptionBase(cx.getCode(), cx.getMessage());
        }
        else if(exception != null) {
            ExceptionMessage defaultMessage = ExceptionMessage.UNKNOWN;
            result.exception = new ExceptionBase(defaultMessage.getCode(), defaultMessage.getMessage());
        }

        result.payload = payload;

        return result;
    }

    public static <T> ResponseBase<T> success() {
        return ResponseBase.of(true, null, null);
    }

    public static <T> ResponseBase<T> success(T payload) {
        return ResponseBase.of(true, null, payload);
    }
}