package com.yigit.social_media.dto.error;

import com.yigit.social_media.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private String errorCode;
    private final Long timestamp = System.currentTimeMillis();

    public ErrorResponse(String message, ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode.getCode();
    }
} 