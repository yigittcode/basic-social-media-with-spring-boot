package com.yigit.social_media.dto.post.response;

import java.time.LocalDateTime;
import java.util.List;
import com.yigit.social_media.dto.image.response.ImageResponseDTO;
import com.yigit.social_media.enums.PostStatus;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class AdminPostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private Long authorId;
    private PostStatus status;
    private List<ImageResponseDTO> images;
    private int likeCount;
    private int commentCount;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
