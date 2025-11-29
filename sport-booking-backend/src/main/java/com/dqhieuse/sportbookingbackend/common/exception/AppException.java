package com.dqhieuse.sportbookingbackend.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AppException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public AppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
