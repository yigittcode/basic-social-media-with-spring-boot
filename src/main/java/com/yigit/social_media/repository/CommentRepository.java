package com.yigit.social_media.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yigit.social_media.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId(Long postId);

    List<Comment> findAllByAuthorId(Long userId);
    
}
