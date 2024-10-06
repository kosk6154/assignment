package com.example.assignment.common.model.exception;

import lombok.Getter;

/**
 * exception은 common에서 공유되기보다는 아래가 맞을거같긴 한데,
 * common 객체에서 enum 파싱 실패 등의 오류에 대해 대응하긴 해야해서 우선 위로 올린다.
 *
 * common 수준에서 공유되는 오류와 api 레벨에서 사용되는 오류를 구분하여 관리할지는 고민 필요
 */
@Getter
public class CoordinationException extends RuntimeException {
    private final int code;
    private final String message;
    private final int status;

    public CoordinationException(ExceptionMessage exceptionMessage) {
        this.code = exceptionMessage.getCode();
        this.message = exceptionMessage.getMessage();
        this.status = exceptionMessage.getStatus();
    }

    public CoordinationException(ExceptionMessage exceptionMessage, String message) {
        this.code = exceptionMessage.getCode();
        this.message = message;
        this.status = exceptionMessage.getStatus();
    }
}
