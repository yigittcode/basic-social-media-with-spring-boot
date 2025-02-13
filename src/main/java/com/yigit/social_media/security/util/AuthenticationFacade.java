package com.yigit.social_media.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.yigit.social_media.model.User;
import com.yigit.social_media.security.user.PrincipalUser;
@Component
public class AuthenticationFacade {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        return principalUser.getUser();
    }
}
