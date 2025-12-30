package org.datpham.orderservice.exception;

import org.datpham.orderservice.common.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BaseResponse<Object>> handleBusinessException(ApplicationException e, ServletWebRequest servletWebRequest) {
        return ResponseEntity.ok().body(new BaseResponse<>(null, e.getMessage(), null));
    }
}
