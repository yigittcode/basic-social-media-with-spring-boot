package com.yigit.social_media.mapper;

import org.springframework.stereotype.Component;
import com.yigit.social_media.model.User;
import com.yigit.social_media.dto.user.response.UserResponseDTO;
import com.yigit.social_media.dto.user.response.AdminUserResponseDTO;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponseDTO toUserDTO(User user) {
        if (user == null) return null;
        
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        return dto;
    }

    public AdminUserResponseDTO toAdminDTO(User user) {
        if (user == null) return null;
        
        AdminUserResponseDTO dto = new AdminUserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        return dto;
    }

    public List<UserResponseDTO> toUserDTOList(List<User> users) {
        if (users == null) return null;
        return users.stream()
            .map(this::toUserDTO)
            .collect(Collectors.toList());
    }

    public List<AdminUserResponseDTO> toAdminDTOList(List<User> users) {
        if (users == null) return null;
        return users.stream()
            .map(this::toAdminDTO)
            .collect(Collectors.toList());
    }
}
