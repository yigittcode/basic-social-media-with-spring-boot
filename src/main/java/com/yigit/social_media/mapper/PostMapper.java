package com.yigit.social_media.mapper;

import org.springframework.stereotype.Component;
import com.yigit.social_media.model.Post;
import com.yigit.social_media.dto.post.response.PostResponseDTO;
import com.yigit.social_media.dto.post.response.AdminPostResponseDTO;
import com.yigit.social_media.dto.user.response.UserResponseDTO;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostMapper {
    
    private final ImageMapper imageMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    public PostResponseDTO toPostResponseDTO(Post post) {
        if (post == null) return null;
        
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setStatus(post.getStatus());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setImages(imageMapper.toImageResponseDTOList(post.getImages()));
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            dto.setComments(commentMapper.toCommentResponseDTOList(post.getComments()));
        }
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }

    public AdminPostResponseDTO toAdminDTO(Post post) {
        if (post == null) return null;
        
        AdminPostResponseDTO dto = new AdminPostResponseDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorUsername(post.getAuthor().getUsername());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setStatus(post.getStatus());
        dto.setImages(imageMapper.toImageResponseDTOList(post.getImages()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }

    public List<PostResponseDTO> toPostResponseDTOList(List<Post> posts) {
        if (posts == null) return Collections.emptyList();
        return posts.stream()
                .map(this::toPostResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AdminPostResponseDTO> toAdminDTOList(List<Post> posts) {
        if (posts == null) return Collections.emptyList();
        return posts.stream()
                .map(this::toAdminDTO)
                .collect(Collectors.toList());
    }
}