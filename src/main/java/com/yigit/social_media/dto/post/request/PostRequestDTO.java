package com.yigit.social_media.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostRequestDTO {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 10000, message = "Content cannot exceed 10000 characters")
    private String content;
    private MultipartFile[] images;
}
