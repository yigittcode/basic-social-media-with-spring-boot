package com.yigit.social_media.service.post;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yigit.social_media.dto.post.request.PostRequestDTO;
import com.yigit.social_media.dto.post.response.PostResponseDTO;
import com.yigit.social_media.security.util.AuthenticationFacade;
import com.yigit.social_media.model.User;
import com.yigit.social_media.model.Post;
import com.yigit.social_media.enums.PostStatus;
import com.yigit.social_media.repository.PostRepository;
import com.yigit.social_media.mapper.PostMapper;
import com.yigit.social_media.service.image.ImageService;
import com.yigit.social_media.model.Image;
import com.yigit.social_media.exception.ResourceNotFoundException;
import com.yigit.social_media.enums.RoleTypes;
import com.yigit.social_media.exception.UnauthorizedException;
import com.yigit.social_media.security.util.SecurityUtils;

@Service
@Transactional
public class PostService implements IPostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AuthenticationFacade authenticationFacade;
    private final ImageService imageService;
    private final SecurityUtils securityUtils;
    private final String uploadDir;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    public PostService(
            PostRepository postRepository, 
            PostMapper postMapper, 
            AuthenticationFacade authenticationFacade, 
            ImageService imageService, 
            SecurityUtils securityUtils,
            @Value("${file.upload-dir}") String uploadDir) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.authenticationFacade = authenticationFacade;
        this.imageService = imageService;
        this.securityUtils = securityUtils;
        this.uploadDir = uploadDir;
    }

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO) {
        User currentUser = authenticationFacade.getCurrentUser();
        Post post = new Post();
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        post.setAuthor(currentUser);
        post.setStatus(PostStatus.PENDING);
        
        // First save the post
        Post savedPost = postRepository.save(post);
        
        // If there are images, process and save them
        if (postRequestDTO.getImages() != null && postRequestDTO.getImages().length > 0) {
            // Sadece resimleri kaydet, DTO'ları görmezden gel
            imageService.saveImages(postRequestDTO.getImages(), savedPost.getId());
            // Post'u tekrar yükle ki yeni eklenen resimlerle gelsin
            savedPost = postRepository.findById(savedPost.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        }
        
        return postMapper.toPostResponseDTO(savedPost);
    }

    @Override
    public PostResponseDTO updatePost(Long postId, PostRequestDTO postRequestDTO) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        // Güvenlik kontrolü
        securityUtils.checkPostOwnership(post);
        
        post.setTitle(postRequestDTO.getTitle());
        post.setContent(postRequestDTO.getContent());
        postRepository.save(post);
        return postMapper.toPostResponseDTO(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        User currentUser = authenticationFacade.getCurrentUser();
        
        if (!post.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(RoleTypes.ADMIN)) {
            throw new UnauthorizedException("You are not authorized to delete this post");
        }
        
        try {
            // Önce resimleri sil
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                for (Image image : post.getImages()) {
                    String fileName = image.getUrl().substring("/uploads/images/".length());
                    Files.deleteIfExists(Paths.get(uploadDir, fileName));
                }
                imageService.deleteImagesByPostId(postId);
            }
            
            postRepository.delete(post);
        } catch (IOException e) {
            logger.error("Error while deleting post images: ", e);
            throw new RuntimeException("Failed to delete post images");
        }
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        List<Post> posts = postRepository.findAllByStatus(PostStatus.APPROVED);
        return postMapper.toPostResponseDTOList(posts);
    }

    @Override
    public List<PostResponseDTO> getAllPostsByUser(Long userId) {
        User currentUser = authenticationFacade.getCurrentUser();
        boolean isAdminOrOwner = currentUser != null && 
            (currentUser.getRole().equals(RoleTypes.ADMIN) || 
             currentUser.getId().equals(userId));
             
        // Eğer admin veya post sahibi ise tüm postları görebilir
        // Değilse sadece onaylı postları görebilir
        List<Post> posts = isAdminOrOwner ? 
            postRepository.findAllByAuthorId(userId) :
            postRepository.findAllByAuthorIdAndStatus(userId, PostStatus.APPROVED);
        
        return postMapper.toPostResponseDTOList(posts);
    }

    @Override
    public List<PostResponseDTO> myPosts() {
       User currentUser = authenticationFacade.getCurrentUser();
       List<Post> posts = postRepository.findAllByAuthorId(currentUser.getId());
       return postMapper.toPostResponseDTOList(posts);
    }

    @Override
    public PostResponseDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        // Eğer post onaylı değilse ve kullanıcı admin veya post sahibi değilse 404 ver
        if (!post.getStatus().equals(PostStatus.APPROVED)) {
            User currentUser = authenticationFacade.getCurrentUser();
            boolean isAdminOrOwner = currentUser != null && 
                (currentUser.getRole().equals(RoleTypes.ADMIN) || 
                 post.getAuthor().getId().equals(currentUser.getId()));
                 
            if (!isAdminOrOwner) {
                throw new ResourceNotFoundException("Post not found");
            }
        }
        
        return postMapper.toPostResponseDTO(post);
    }
    
}
