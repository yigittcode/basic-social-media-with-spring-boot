package com.yigit.social_media.dto.user.response;

import java.time.LocalDateTime;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yigit.social_media.enums.RoleTypes;
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private RoleTypes role;

}
