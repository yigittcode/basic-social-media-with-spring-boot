package com.yigit.social_media.service.comment;

import com.yigit.social_media.dto.comment.request.CommentRequestDTO;
import com.yigit.social_media.dto.comment.response.CommentResponseDTO;
import java.util.List;

public interface ICommentService {
    CommentResponseDTO createComment(Long postId, CommentRequestDTO commentRequestDTO);
    CommentResponseDTO updateComment(Long commentId, CommentRequestDTO commentRequestDTO);
    void deleteComment(Long commentId);
    List<CommentResponseDTO> getCommentsByPost(Long postId);
    List<CommentResponseDTO> getCommentsByUser(Long userId);
    List<CommentResponseDTO> getMyComments();
} 