package com.yigit.social_media.service.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yigit.social_media.dto.comment.request.CommentRequestDTO;
import com.yigit.social_media.dto.comment.response.CommentResponseDTO;
import com.yigit.social_media.model.Comment;
import com.yigit.social_media.model.Post;
import com.yigit.social_media.model.User;
import com.yigit.social_media.repository.CommentRepository;
import com.yigit.social_media.repository.PostRepository;
import com.yigit.social_media.mapper.CommentMapper;
import com.yigit.social_media.security.util.AuthenticationFacade;
import com.yigit.social_media.security.util.SecurityUtils;
import com.yigit.social_media.exception.ResourceNotFoundException;
import com.yigit.social_media.exception.UnauthorizedException;
import com.yigit.social_media.enums.PostStatus;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final AuthenticationFacade authenticationFacade;
    private final SecurityUtils securityUtils;
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Override
    public CommentResponseDTO createComment(Long postId, CommentRequestDTO commentRequestDTO) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        // Post onaylı değilse 404 hatası ver
        if (!post.getStatus().equals(PostStatus.APPROVED)) {
            throw new ResourceNotFoundException("Post not found or not available");
        }

        User currentUser = authenticationFacade.getCurrentUser();
        Comment comment = new Comment();
        comment.setContent(commentRequestDTO.getContent());
        comment.setAuthor(currentUser);
        comment.setPost(post);
        
        try {
            Comment savedComment = commentRepository.save(comment);
            return commentMapper.toCommentResponseDTO(savedComment);
        } catch (Exception e) {
            logger.error("Error while saving comment: ", e);
            throw new RuntimeException("Failed to save comment");
        }
    }

    @Override
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO commentRequestDTO) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
            
        // Güvenlik kontrolü
        securityUtils.checkCommentOwnership(comment);
        
        comment.setContent(commentRequestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toCommentResponseDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
            
        // Güvenlik kontrolü
        securityUtils.checkCommentOwnership(comment);
        
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        
        // Post onaylı değilse 404 hatası ver
        if (!post.getStatus().equals(PostStatus.APPROVED)) {
            throw new ResourceNotFoundException("Post not found or not available");
        }
        
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return commentMapper.toCommentResponseDTOList(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getCommentsByUser(Long userId) {
        List<Comment> comments = commentRepository.findAllByAuthorId(userId);
        return commentMapper.toCommentResponseDTOList(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getMyComments() {
        User currentUser = authenticationFacade.getCurrentUser();
        List<Comment> comments = commentRepository.findAllByAuthorId(currentUser.getId());
        return commentMapper.toCommentResponseDTOList(comments);
    }
} 