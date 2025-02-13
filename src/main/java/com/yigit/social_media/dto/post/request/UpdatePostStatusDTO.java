package com.yigit.social_media.dto.post.request;

import com.yigit.social_media.enums.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePostStatusDTO {
    @NotNull(message = "Status cannot be null")
    private PostStatus status;
} 