package com.yigit.social_media.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import com.yigit.social_media.model.Comment;
import com.yigit.social_media.dto.comment.response.CommentResponseDTO;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    
    private final UserMapper userMapper;

    public CommentResponseDTO toCommentResponseDTO(Comment comment) {
        if (comment == null) return null;
        
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthorUsername(comment.getAuthor().getUsername());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }

    public List<CommentResponseDTO> toCommentResponseDTOList(List<Comment> comments) {
        if (comments == null) return Collections.emptyList();
        return comments.stream()
                .map(this::toCommentResponseDTO)
                .collect(Collectors.toList());
    }
} 