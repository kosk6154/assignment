package com.example.assignment.coordinationapi.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공통 응답을 위한 오류 클래스
 * 모든 오류 발생 시 code와 message만을 반환한다.
 * @author kokyeomjae
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class ExceptionBase {
    private Integer code;
    private String message;
}
