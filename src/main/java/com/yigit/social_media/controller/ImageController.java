package com.yigit.social_media.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.dto.image.response.ImageResponseDTO;
import com.yigit.social_media.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/images")
@RequiredArgsConstructor
@Tag(name = "Image Controller", description = "Endpoints for managing images")
public class ImageController {

    private final IImageService imageService;

    @PostMapping("/post/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload images for a post", description = "Uploads multiple images associated with a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images uploaded successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ImageResponseDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseDTO<List<ImageResponseDTO>> uploadImages(
            @Parameter(description = "ID of the post to upload images to") @PathVariable Long postId,
            @Parameter(description = "Array of image files to upload") @RequestParam("images") MultipartFile[] images) {
        List<ImageResponseDTO> savedImages = imageService.saveImages(images, postId);
        return ResponseDTO.success("Images uploaded successfully", savedImages);
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get images by post ID", description = "Retrieves all images associated with a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ImageResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseDTO<List<ImageResponseDTO>> getImagesByPost(@Parameter(description = "ID of the post to retrieve images from") @PathVariable Long postId) {
        List<ImageResponseDTO> images = imageService.getImagesByPostId(postId);
        return ResponseDTO.success("Images retrieved successfully", images);
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Get image by ID", description = "Retrieves a specific image by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ImageResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    public ResponseDTO<ImageResponseDTO> getImage(@Parameter(description = "ID of the image to retrieve") @PathVariable Long imageId) {
        ImageResponseDTO image = imageService.getImageById(imageId);
        return ResponseDTO.success("Image retrieved successfully", image);
    }

    @GetMapping
    @Operation(summary = "Get all images", description = "Retrieves a list of all images.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All images retrieved successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ImageResponseDTO.class))))
    })
    public ResponseDTO<List<ImageResponseDTO>> getAllImages() {
        List<ImageResponseDTO> images = imageService.getAllImages();
        return ResponseDTO.success("All images retrieved successfully", images);
    }

    @PutMapping("/{imageId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update an image", description = "Updates an existing image by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image updated successfully", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ImageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    public ResponseDTO<ImageResponseDTO> updateImage(
            @Parameter(description = "ID of the image to update") @PathVariable Long imageId,
            @Parameter(description = "New image file to replace the old one") @RequestParam("image") MultipartFile image) {
        ImageResponseDTO updatedImage = imageService.updateImage(imageId, image);
        return ResponseDTO.success("Image updated successfully", updatedImage);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete an image by ID", description = "Deletes a specific image by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    public ResponseDTO<Void> deleteImage(@Parameter(description = "ID of the image to delete") @PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseDTO.success("Image deleted successfully", null);
    }

    @DeleteMapping("/post/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete images by post ID", description = "Deletes all images associated with a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All images for post deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseDTO<Void> deleteImagesByPost(@Parameter(description = "ID of the post to delete images from") @PathVariable Long postId) {
        imageService.deleteImagesByPostId(postId);
        return ResponseDTO.success("All images for post deleted successfully", null);
    }
}