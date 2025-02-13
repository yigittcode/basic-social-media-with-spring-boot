package com.yigit.social_media.dto.post.response;

import java.time.LocalDateTime;
import java.util.List;
import com.yigit.social_media.dto.user.response.UserResponseDTO;
import com.yigit.social_media.dto.image.response.ImageResponseDTO;
import com.yigit.social_media.enums.PostStatus;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yigit.social_media.dto.comment.response.CommentResponseDTO;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private List<CommentResponseDTO> comments;
    private List<ImageResponseDTO> images;
    private PostStatus status;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
}
