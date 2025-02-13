package com.yigit.social_media.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDTO {
    @NotBlank(message = "Content cannot be empty")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String content;
} 