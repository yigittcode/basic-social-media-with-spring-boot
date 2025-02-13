package com.yigit.social_media.service.image;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.yigit.social_media.dto.image.response.ImageResponseDTO;
import com.yigit.social_media.model.Post;
import com.yigit.social_media.model.Image;
import com.yigit.social_media.enums.RoleTypes;
import com.yigit.social_media.repository.PostRepository;
import com.yigit.social_media.repository.ImageRepository;
import com.yigit.social_media.exception.ResourceNotFoundException;
import com.yigit.social_media.mapper.ImageMapper;
import com.yigit.social_media.security.util.SecurityUtils;
import com.yigit.social_media.model.User;
import com.yigit.social_media.exception.UnauthorizedException;
import com.yigit.social_media.security.util.AuthenticationFacade;

@Service
@Transactional
public class ImageService implements IImageService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final SecurityUtils securityUtils;
    private final AuthenticationFacade authenticationFacade;
    private final String uploadDir = "uploads/images/";

    public ImageService(PostRepository postRepository, 
                       ImageRepository imageRepository, 
                       ImageMapper imageMapper,
                       SecurityUtils securityUtils,
                       AuthenticationFacade authenticationFacade) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.securityUtils = securityUtils;
        this.authenticationFacade = authenticationFacade;
        createUploadDirectory();
    }

    private void createUploadDirectory() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    @Override
    public List<ImageResponseDTO> saveImages(MultipartFile[] files, Long postId) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("No files were uploaded");
        }

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        securityUtils.checkPostOwnership(post);

        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            
            if (!isImageFile(file)) {
                throw new IllegalArgumentException("File must be an image");
            }

            try {
                String fileName = generateUniqueFileName(file);
                Path targetLocation = Paths.get(uploadDir + fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                Image image = new Image();
                image.setUrl("/uploads/images/" + fileName);
                image.setType(file.getContentType());
                image.setPost(post);
                images.add(imageRepository.save(image));
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image: " + e.getMessage());
            }
        }
        
        return imageMapper.toImageResponseDTOList(images);
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    @Override
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            
        User currentUser = authenticationFacade.getCurrentUser();
        Post post = image.getPost();
        
        // Admin veya post sahibi kontrolü
        if (!post.getAuthor().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(RoleTypes.ADMIN)) {
            throw new UnauthorizedException("You are not authorized to delete this image");
        }
        
        try {
            // URL'den dosya yolunu düzgün şekilde al
            String fileName = image.getUrl().substring("/uploads/images/".length());
            Files.deleteIfExists(Paths.get(uploadDir, fileName));
            
            // Post'un images listesinden kaldır
            post.getImages().remove(image);
            postRepository.save(post);
            
            // Veritabanından sil
            imageRepository.delete(image);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }

    @Override
    public List<ImageResponseDTO> getImagesByPostId(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        return imageMapper.toImageResponseDTOList(post.getImages());
    }

    @Override
    public ImageResponseDTO getImageById(Long imageId) {
        Image image = imageRepository.findById(imageId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            
        return imageMapper.toImageResponseDTO(image);
    }

    @Override
    public List<ImageResponseDTO> getAllImages() {
        return imageMapper.toImageResponseDTOList(imageRepository.findAll());
    }

    @Override
    public ImageResponseDTO updateImage(Long imageId, MultipartFile file) {
        Image image = imageRepository.findById(imageId)
            .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            
        securityUtils.checkPostOwnership(image.getPost());
        
        try {
            Files.deleteIfExists(Paths.get(image.getUrl()));
            
            String newUrl = saveImageFile(file);
            image.setUrl(newUrl);
            image.setType(file.getContentType());
            
            Image updatedImage = imageRepository.save(image);
            return imageMapper.toImageResponseDTO(updatedImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage());
        }
    }

    @Override
    public void deleteImagesByPostId(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            
        securityUtils.checkPostOwnership(post);
        
        // Önce dosya sisteminden resimleri sil
        post.getImages().forEach(image -> {
            try {
                String fileName = image.getUrl().substring("/uploads/images/".length());
                Files.deleteIfExists(Paths.get(uploadDir, fileName));
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image file: " + e.getMessage());
            }
        });
        
        // Veritabanından resimleri sil
        imageRepository.deleteAll(post.getImages());
        
        // Post'un images listesini temizle
        post.clearImages();
        
        // Post'u güncelle
        postRepository.save(post);
    }

    private String saveImageFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }
}
