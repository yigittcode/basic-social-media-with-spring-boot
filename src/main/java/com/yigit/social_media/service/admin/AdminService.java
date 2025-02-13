package com.yigit.social_media.service.admin;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;

import com.yigit.social_media.dto.post.response.AdminPostResponseDTO;
import com.yigit.social_media.dto.user.response.AdminUserResponseDTO;
import com.yigit.social_media.enums.PostStatus;
import com.yigit.social_media.enums.RoleTypes;
import com.yigit.social_media.repository.PostRepository;    
import com.yigit.social_media.repository.UserRepository;
import java.util.stream.Collectors;
import com.yigit.social_media.mapper.PostMapper;
import com.yigit.social_media.mapper.UserMapper;
import com.yigit.social_media.model.Post;
import com.yigit.social_media.exception.ResourceNotFoundException;
import com.yigit.social_media.repository.CommentRepository;
import com.yigit.social_media.repository.ImageRepository;
import com.yigit.social_media.model.User;
import com.yigit.social_media.exception.UnauthorizedException;
import com.yigit.social_media.security.util.SecurityUtils;
import com.yigit.social_media.model.Image;

@Service
@Transactional
public class AdminService implements IAdminService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;
    private final SecurityUtils securityUtils;
    private final String uploadDir;

    public AdminService(PostRepository postRepository, 
                       UserRepository userRepository, 
                       PostMapper postMapper, 
                       UserMapper userMapper, 
                       CommentRepository commentRepository,
                       ImageRepository imageRepository,
                       SecurityUtils securityUtils,
                       @Value("${file.upload-dir}") String uploadDir) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.userMapper = userMapper;
        this.commentRepository = commentRepository;
        this.imageRepository = imageRepository;
        this.securityUtils = securityUtils;
        this.uploadDir = uploadDir;
    }

    private void checkAdminAccess() {
        User currentUser = securityUtils.getCurrentUser();
        if (!securityUtils.isAdmin(currentUser)) {
            throw new UnauthorizedException("Admin access required");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminPostResponseDTO> getAllPosts(PostStatus status) {
        checkAdminAccess();
        
        List<Post> posts = (status == null) ? 
            postRepository.findAll() : 
            postRepository.findAllByStatus(status);
            
        return postMapper.toAdminDTOList(posts);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminUserResponseDTO> getAllUsers(RoleTypes role) {
        checkAdminAccess();
        
        List<User> users = (role == null) ?
            userRepository.findAll() :
            userRepository.findAllByRole(role);
            
        return users.stream()
            .map(userMapper::toAdminDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public AdminPostResponseDTO updatePostStatus(Long postId, PostStatus status) {
        checkAdminAccess();
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        
        post.setStatus(status);
        post = postRepository.save(post);
        return postMapper.toAdminDTO(post);
    }

    @Transactional
    @Override
    public void removePost(Long postId) {
        checkAdminAccess();
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        // Önce resimleri sil
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            post.getImages().forEach(image -> {
                try {
                    Files.deleteIfExists(Paths.get(image.getUrl()));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete image file: " + e.getMessage());
                }
            });
            imageRepository.deleteAll(post.getImages());
            post.getImages().clear();
        }
        
        postRepository.delete(post);
    }

    @Transactional
    @Override
    public void removeComment(Long commentId) {
        checkAdminAccess();
        commentRepository.findById(commentId)
            .ifPresent(commentRepository::delete);
    }

    @Transactional
    @Override
    public void removeUser(Long userId) {
        checkAdminAccess();
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
        // Admin kendisini silemesin
        if (user.getId().equals(securityUtils.getCurrentUser().getId())) {
            throw new UnauthorizedException("Admin cannot delete their own account");
        }
        
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public void removeImage(Long imageId) {
        checkAdminAccess();
        
        Image image = imageRepository.findById(imageId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            
        try {
            String fileName = image.getUrl().substring("/uploads/images/".length());
            Files.deleteIfExists(Paths.get(uploadDir, fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image file: " + e.getMessage());
        }
        
        imageRepository.delete(image);
    }

    @Transactional
    @Override
    public AdminUserResponseDTO updateUserRoles(Long userId, RoleTypes role) {
        checkAdminAccess();
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
        // Admin kendi rolünü değiştiremesin
        if (user.getId().equals(securityUtils.getCurrentUser().getId())) {
            throw new UnauthorizedException("Admin cannot change their own role");
        }
        
        user.setRole(role);
        user = userRepository.save(user);
        return userMapper.toAdminDTO(user);
    }
}
