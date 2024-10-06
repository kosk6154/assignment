package com.example.assignment.coordinationapi.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionBase {
    private Integer code;
    private String message;
}
