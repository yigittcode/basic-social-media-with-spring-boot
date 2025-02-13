package com.yigit.social_media.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yigit.social_media.dto.user.request.UserRequestDTO;
import com.yigit.social_media.dto.user.response.UserResponseDTO;
import com.yigit.social_media.enums.RoleTypes;
import com.yigit.social_media.exception.ResourceNotFoundException;
import com.yigit.social_media.exception.UnauthorizedException;
import com.yigit.social_media.model.User;
import com.yigit.social_media.repository.UserRepository;
import com.yigit.social_media.mapper.UserMapper;
import com.yigit.social_media.security.util.SecurityUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public UserService(UserRepository userRepository,
                      UserMapper userMapper,
                      PasswordEncoder passwordEncoder,
                      SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }

    @Override
    public UserResponseDTO updateUser(UserRequestDTO userRequestDTO) {
        User currentUser = securityUtils.getCurrentUser();
        
        if (userRequestDTO.getUsername() != null &&
            !currentUser.getUsername().equals(userRequestDTO.getUsername()) &&
            userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if new email is already taken by another user
        if (userRequestDTO.getEmail() != null &&
            !currentUser.getEmail().equals(userRequestDTO.getEmail()) &&
            userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Update fields if provided
        if (userRequestDTO.getUsername() != null) {
            currentUser.setUsername(userRequestDTO.getUsername());
        }
        if (userRequestDTO.getEmail() != null) {
            currentUser.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(currentUser);
        return userMapper.toUserDTO(updatedUser);
    }

    @Override
    public void deleteUser() {
        User currentUser = securityUtils.getCurrentUser();
        userRepository.delete(currentUser);
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toUserDTO(user);
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        return userMapper.toUserDTO(securityUtils.getCurrentUser());
    }

    @Override
    public List<UserResponseDTO> searchUsers(String query) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
        return userMapper.toUserDTOList(users);
    }
}
