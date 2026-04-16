package com.hit.springboot.exception.extended;

public class DuplicateResourceException extends AppException {

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(409, String.format("%s đã tồn tại với %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
