package com.yigit.social_media.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.dto.post.request.PostRequestDTO;
import com.yigit.social_media.dto.post.response.PostResponseDTO;
import com.yigit.social_media.service.post.IPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "Post management endpoints")
public class PostController {

    private final IPostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new post", description = "Creates a new post with optional images")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Post created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseDTO<PostResponseDTO> createPost(
        @RequestPart("title") String title,
        @RequestPart("content") String content,
        @RequestPart(value = "images", required = false) MultipartFile[] images) {
        
        PostRequestDTO postRequestDTO = new PostRequestDTO();
        postRequestDTO.setTitle(title);
        postRequestDTO.setContent(content);
        postRequestDTO.setImages(images);
        
        PostResponseDTO createdPost = postService.createPost(postRequestDTO);
        return ResponseDTO.success("Post created successfully", createdPost);
    }

    @Operation(
        summary = "Get all posts",
        description = "Retrieves all approved posts"
    )
    @GetMapping
    public ResponseDTO<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseDTO.success("Posts retrieved successfully", posts);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO<List<PostResponseDTO>> getMyPosts() {
        List<PostResponseDTO> posts = postService.myPosts();
        return ResponseDTO.success("Your posts retrieved successfully", posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseDTO<List<PostResponseDTO>> getUserPosts(@PathVariable Long userId) {
        List<PostResponseDTO> posts = postService.getAllPostsByUser(userId);
        return ResponseDTO.success("User's posts retrieved successfully", posts);
    }

    @GetMapping("/{postId}")
    public ResponseDTO<PostResponseDTO> getPostById(@PathVariable Long postId) {
        PostResponseDTO post = postService.getPostById(postId);
        return ResponseDTO.success("Post retrieved successfully", post);
    }

    @PatchMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO<PostResponseDTO> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDTO postRequestDTO) {
        PostResponseDTO updatedPost = postService.updatePost(postId, postRequestDTO);
        return ResponseDTO.success("Post updated successfully", updatedPost);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseDTO<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseDTO.success("Post deleted successfully", null);
    }
} 