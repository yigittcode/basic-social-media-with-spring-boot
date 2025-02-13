package com.yigit.social_media.dto.user.response;

import com.yigit.social_media.enums.RoleTypes;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminUserResponseDTO extends UserResponseDTO {
    private String email;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
