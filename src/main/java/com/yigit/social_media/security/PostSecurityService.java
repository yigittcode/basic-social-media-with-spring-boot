package com.yigit.social_media.security;

import org.springframework.stereotype.Service;
import com.yigit.social_media.exception.ResourceNotFoundException;
import com.yigit.social_media.model.Post;
import com.yigit.social_media.model.User;
import com.yigit.social_media.repository.PostRepository;
import com.yigit.social_media.security.util.AuthenticationFacade;

@Service("postSecurityService")
public class PostSecurityService {
    
    private  PostRepository postRepository;
    private  AuthenticationFacade authenticationFacade;

    public PostSecurityService(PostRepository postRepository, AuthenticationFacade authenticationFacade) {
        this.postRepository = postRepository;
        this.authenticationFacade = authenticationFacade;
    }
    
    public boolean isPostOwner(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        User currentUser = authenticationFacade.getCurrentUser();
        return post.getAuthor().getId().equals(currentUser.getId());
    }
} 