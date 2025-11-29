package com.dqhieuse.sportbookingbackend.common.exception;

import com.dqhieuse.sportbookingbackend.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice // Annotation bắt lỗi toàn bộ ứng dụng
public class GlobalExceptionHandler {

    // Bắt lỗi do mình tự ném ra (AppException)
    @ExceptionHandler(value = AppException.class)
    public ApiResponse<Void> handleAppException(AppException e) {
        return ApiResponse.<Void>builder()
                .code(e.getStatus().value())
                .msg(e.getMessage())
                .build();
    }

    // Bắt lỗi Validation (@NotNull, @Email...)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage(); // Lấy message trong DTO

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(400)
                .msg(enumKey)
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    // Bắt tất cả các lỗi lạ khác (500 Internal Server Error)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnwantedException(Exception e) {
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(500)
                .msg("Uncategorized error: " + e.getMessage()) // Dev chỉ xem log, đừng show chi tiết cho User
                .build();

        return ResponseEntity.internalServerError().body(apiResponse);
    }
}