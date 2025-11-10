package com.springcloud.msvc_payments.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }


    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.CONFLICT;
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }

}
