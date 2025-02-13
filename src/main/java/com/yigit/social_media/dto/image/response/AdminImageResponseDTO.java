package com.yigit.social_media.dto.image.response;

import java.time.LocalDateTime;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class AdminImageResponseDTO {
    private Long id;
    private String url;
    private String type;
    private Long postId;
    private String postTitle;
    private String authorUsername;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
} 