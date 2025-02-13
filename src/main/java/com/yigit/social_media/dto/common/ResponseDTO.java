package com.yigit.social_media.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    
    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(true, "Success", data);
    }
    
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(true, message, data);
    }
    
    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(false, message, null);
    }
} 