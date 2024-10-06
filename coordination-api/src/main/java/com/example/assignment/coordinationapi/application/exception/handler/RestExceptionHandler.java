package com.example.assignment.coordinationapi.application.exception.handler;


import com.example.assignment.common.model.exception.CoordinationException;
import com.example.assignment.common.model.exception.ExceptionMessage;
import com.example.assignment.coordinationapi.application.model.ResponseBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 오류 핸들러
 * ControllerAdvice를 이용하여 오류처리 한다.
 * 서비스를 위한 api와 admin용 api가 같이 들어있으므로 오류 메시지는 최대한 숨긴다.
 * @param <T>
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler<T> {

    @ExceptionHandler(CoordinationException.class)
    @ResponseBody
    public ResponseEntity<ResponseBase<T>> handleCommonException(CoordinationException ex) {
        log.error("CoordinationException: ", ex);

        return ResponseEntity
                .status(ex.getStatus())
                .body(
                        ResponseBase.of(false, ex, null));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ResponseBase<T>> handleException(Exception ex) {
        log.error("예기치 못한 오류 발생", ex);

        return ResponseEntity
                .internalServerError()
                .body(
                        ResponseBase.of(false, new CoordinationException(ExceptionMessage.UNKNOWN), null));
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<ResponseBase<T>> handleBindException(BindException ex) {
        log.error("Bind Exception: ", ex);

        return ResponseEntity
                .badRequest()
                .body(
                        ResponseBase.of(false, new CoordinationException(ExceptionMessage.INVALID_PARAMETER), null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ResponseBase<T>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: ", ex);

        return ResponseEntity
                .badRequest()
                .body(
                        ResponseBase.of(false, new CoordinationException(ExceptionMessage.BAD_REQUEST), null));
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseBody
    public ResponseEntity<ResponseBase<T>> servletRequestBindingException(ServletRequestBindingException ex) {
        log.error("ServletRequestBindingException: ", ex);

        return ResponseEntity
                .badRequest()
                .body(
                        ResponseBase.of(false, new CoordinationException(ExceptionMessage.INVALID_PARAMETER), null));
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ResponseBase<T>> typeMismatchException(TypeMismatchException ex) {
        log.error("TypeMismatchException: ", ex);

        return ResponseEntity
                .badRequest()
                .body(
                        ResponseBase.of(false, new CoordinationException(ExceptionMessage.BAD_REQUEST), null));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public ResponseEntity<ResponseBase<T>> httpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        log.error("HttpMediaTypeNotAcceptableException: ", ex);

        return ResponseEntity
                .badRequest()
                .body(
                        ResponseBase.of(false, new CoordinationException(ExceptionMessage.BAD_REQUEST), null));
    }
}
