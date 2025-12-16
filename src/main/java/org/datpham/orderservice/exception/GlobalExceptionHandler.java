package org.datpham.orderservice.exception;

import org.datpham.orderservice.dto.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApplicationException.class)
    ResponseEntity<BaseResponse> handlingRuntimeException(ApplicationException exception){
        BaseResponse apiResponse = new BaseResponse();
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
