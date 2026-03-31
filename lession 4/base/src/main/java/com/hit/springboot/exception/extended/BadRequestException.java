package com.hit.springboot.exception.extended;

public class BadRequestException extends AppException {

    public BadRequestException(String message) {
        super(400, message);
    }
}
