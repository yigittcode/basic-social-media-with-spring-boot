package com.yigit.social_media.service.admin;

import java.util.List;

import com.yigit.social_media.dto.post.response.AdminPostResponseDTO;
import com.yigit.social_media.dto.user.response.AdminUserResponseDTO;
import com.yigit.social_media.enums.PostStatus;
import com.yigit.social_media.enums.RoleTypes;

public interface IAdminService {
    List<AdminPostResponseDTO> getAllPosts(PostStatus status);

    List<AdminUserResponseDTO> getAllUsers(RoleTypes role);

    AdminPostResponseDTO updatePostStatus(Long postId, PostStatus status);

    void removePost(Long postId);

    void removeComment(Long commentId);

    void removeUser(Long userId);

    void removeImage(Long imageId);

    AdminUserResponseDTO updateUserRoles(Long userId, RoleTypes role);
}


