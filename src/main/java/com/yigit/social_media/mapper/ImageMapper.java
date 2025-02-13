package com.yigit.social_media.mapper;

import org.springframework.stereotype.Component;
import com.yigit.social_media.model.Image;
import com.yigit.social_media.dto.image.response.ImageResponseDTO;
import com.yigit.social_media.dto.image.response.AdminImageResponseDTO;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageMapper {

    public ImageResponseDTO toImageResponseDTO(Image image) {
        if (image == null) return null;
        
        ImageResponseDTO dto = new ImageResponseDTO();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setType(image.getType());
        dto.setCreatedAt(image.getCreatedAt());
        dto.setUpdatedAt(image.getUpdatedAt());
        return dto;
    }

    public AdminImageResponseDTO toAdminImageResponseDTO(Image image) {
        if (image == null) return null;
        
        AdminImageResponseDTO dto = new AdminImageResponseDTO();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setType(image.getType());
        dto.setCreatedAt(image.getCreatedAt());
        dto.setUpdatedAt(image.getUpdatedAt());
        
        if (image.getPost() != null) {
            dto.setPostId(image.getPost().getId());
            dto.setPostTitle(image.getPost().getTitle());
            dto.setAuthorUsername(image.getPost().getAuthor().getUsername());
        }
        
        return dto;
    }

    public List<ImageResponseDTO> toImageResponseDTOList(List<Image> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(this::toImageResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AdminImageResponseDTO> toAdminImageResponseDTOList(List<Image> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .map(this::toAdminImageResponseDTO)
                .collect(Collectors.toList());
    }
} 