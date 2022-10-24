package altera.food.ordering.util;

import altera.food.ordering.constant.AppConstant;
import altera.food.ordering.domain.common.ApiResponse;
import altera.food.ordering.domain.common.ApiResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseUtil {

    private ResponseUtil() {}

    public static <T> ResponseEntity<Object> build(AppConstant.ResponseCode responseCode, T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(build(responseCode, data), httpStatus);
    }

    private static <T> ApiResponse<T> build(AppConstant.ResponseCode responseCode, T data) {
        return ApiResponse.<T>builder()
            .status(ApiResponseStatus.builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .build())
            .timestamp(LocalDateTime.now())
            .data(data)
            .build();
    }
    
    
}
