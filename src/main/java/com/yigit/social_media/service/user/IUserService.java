package com.yigit.social_media.service.user;

import java.util.List;
import com.yigit.social_media.dto.user.request.UserRequestDTO;
import com.yigit.social_media.dto.user.response.UserResponseDTO;

public interface IUserService {
    UserResponseDTO updateUser(UserRequestDTO userRequestDTO);
    void deleteUser();
    UserResponseDTO getUserById(Long userId);
    UserResponseDTO getCurrentUser();
    List<UserResponseDTO> searchUsers(String query);
}
