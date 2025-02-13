package com.yigit.social_media.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yigit.social_media.enums.PostStatus;
import com.yigit.social_media.model.Post;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByStatus(PostStatus status);

    List<Post> findAllByAuthorId(Long userId);
    
    List<Post> findAllByAuthorIdAndStatus(Long userId, PostStatus status);
}
