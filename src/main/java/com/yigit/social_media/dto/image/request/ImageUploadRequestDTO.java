package com.yigit.social_media.dto.image.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;

@Data
public class ImageUploadRequestDTO {
    @NotNull(message = "Image file cannot be null")
    private MultipartFile file;
    
    @NotNull(message = "Post ID cannot be null")
    private Long postId;
} 