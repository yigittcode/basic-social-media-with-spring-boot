package com.yigit.social_media.security.util;

import org.springframework.stereotype.Component;
import com.yigit.social_media.model.Post;
import com.yigit.social_media.model.User;
import com.yigit.social_media.model.Comment;
import com.yigit.social_media.enums.RoleTypes;
import com.yigit.social_media.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    
    private final AuthenticationFacade authenticationFacade;

    public void checkPostOwnership(Post post) {
        User currentUser = getCurrentUser();
        if (!post.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(RoleTypes.ADMIN)) {
            throw new UnauthorizedException("You are not authorized to modify this post");
        }
    }
    
    public boolean isPostOwner(Post post, User user) {
        return post.getAuthor().getId().equals(user.getId());
    }
    
    public boolean isAdmin(User user) {
        return user.getRole().equals(RoleTypes.ADMIN);
    }
    
    public User getCurrentUser() {
        return authenticationFacade.getCurrentUser();
    }

    public void checkCommentOwnership(Comment comment) {
        User currentUser = getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(RoleTypes.ADMIN)) {
            throw new UnauthorizedException("You are not authorized to modify this comment");
        }
    }

    public void checkAdminAccess() {
        User currentUser = getCurrentUser();
        if (!currentUser.getRole().equals(RoleTypes.ADMIN)) {
            throw new UnauthorizedException("Admin access required");
        }
    }
} 