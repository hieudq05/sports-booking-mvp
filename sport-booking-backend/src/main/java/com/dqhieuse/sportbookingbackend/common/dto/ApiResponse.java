package com.dqhieuse.sportbookingbackend.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Builder.Default
    private int code = 1000;
    private String msg = "Success";
    private T data;

    // Success with default success code, msg and return data
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .build();
    }

    // Success with default success code and return data, msg
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .msg(message)
                .data(data)
                .build();
    }

    // Success created with default created code and return data, msg
    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .code(201)
                .msg(message)
                .data(data)
                .build();
    }

    // Error with code, data and msg
    public static <T> ApiResponse<T> error(T data, String message, int code) {
        return ApiResponse.<T>builder()
                .code(code)
                .msg(message)
                .data(data)
                .build();
    }
}
