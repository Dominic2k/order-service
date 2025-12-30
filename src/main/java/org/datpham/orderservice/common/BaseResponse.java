package org.datpham.orderservice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class BaseResponse<T> {
    T data;
    String message;
    Map<String, String> fieldErrors;
}
