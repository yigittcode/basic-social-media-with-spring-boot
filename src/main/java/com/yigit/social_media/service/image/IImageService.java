package com.yigit.social_media.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.yigit.social_media.dto.image.response.ImageResponseDTO;

public interface IImageService {

    List<ImageResponseDTO> saveImages(MultipartFile[] images, Long postId);

    void deleteImage(Long imageId);

    List<ImageResponseDTO> getImagesByPostId(Long postId);

    ImageResponseDTO getImageById(Long imageId);

    List<ImageResponseDTO> getAllImages();

    ImageResponseDTO updateImage(Long imageId, MultipartFile image);

    void deleteImagesByPostId(Long postId);

}