package com.example.assignment.coordinationapi.application.model;

import com.example.assignment.common.model.exception.CoordinationException;
import com.example.assignment.common.model.exception.ExceptionMessage;
import lombok.Getter;

/**
 * 공통으로 사용하는 응답 반환 객체
 *
 * @author kokyeomjae
 * @version 0.0.1
 * @param <T>
 */
@Getter
public class ResponseBase<T> {
    private boolean success; // 성공일 경우 true
    private ExceptionBase exception; // 성공이 아닐 경우만 객체가 담김, 그렇지 않을 경우 null
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