package com.hit.springboot.exception.extended;

public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(404, String.format("%s không tìm thấy với %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
