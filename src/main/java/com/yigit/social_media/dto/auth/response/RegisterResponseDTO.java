package com.yigit.social_media.dto.auth.response;

import lombok.Data;

@Data
public class RegisterResponseDTO {
    private String token;
    private Long expiresIn;
}
