package com.yigit.social_media.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    
    RESOURCE_NOT_FOUND("NOT_FOUND_ERROR", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_ACCESS("FORBIDDEN_ERROR", HttpStatus.FORBIDDEN),
    VALIDATION_ERROR("VALIDATION_ERROR", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    POST_NOT_AVAILABLE("POST_NOT_AVAILABLE", HttpStatus.NOT_FOUND),
    FILE_OPERATION_ERROR("FILE_OPERATION_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_TYPE("INVALID_FILE_TYPE", HttpStatus.BAD_REQUEST),
    OPERATION_NOT_ALLOWED("OPERATION_NOT_ALLOWED", HttpStatus.FORBIDDEN);

    private final String code;
    private final HttpStatus status;

    ErrorCode(String code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }
} 