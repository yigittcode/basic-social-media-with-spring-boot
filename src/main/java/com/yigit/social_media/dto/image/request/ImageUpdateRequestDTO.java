package com.yigit.social_media.dto.image.request;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class ImageUpdateRequestDTO {
    @Size(max = 255, message = "URL cannot exceed 255 characters")
    private String url;
    
    @Size(max = 50, message = "Type cannot exceed 50 characters")
    private String type;
} 