package com.yigit.social_media.controller.admin;

import org.springframework.web.bind.annotation.*;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.dto.post.response.AdminPostResponseDTO;
import com.yigit.social_media.service.admin.AdminService;
import com.yigit.social_media.enums.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/admin/posts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Posts", description = "Operations related to post management by administrators")
public class AdminPostController {

    private final AdminService adminService;

    @GetMapping
    @Operation(summary = "Get all posts", description = "Retrieves a list of all posts, optionally filtered by status. Only accessible to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts")
    })
    public ResponseDTO<List<AdminPostResponseDTO>> getAllPosts(
            @Parameter(description = "Status to filter posts by (optional)") @RequestParam(required = false) PostStatus status) {
        List<AdminPostResponseDTO> posts = adminService.getAllPosts(status);
        return ResponseDTO.success("Posts retrieved successfully", posts);
    }

    @PatchMapping("/{postId}/status")
    @Operation(summary = "Update post status", description = "Updates the status of a specific post. Only accessible to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid post ID or status supplied"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseDTO<AdminPostResponseDTO> updatePostStatus(
            @Parameter(description = "ID of the post to be updated") @PathVariable Long postId,
            @Parameter(description = "New status for the post") @RequestParam PostStatus status) {
        AdminPostResponseDTO updatedPost = adminService.updatePostStatus(postId, status);
        return ResponseDTO.success("Post status updated successfully", updatedPost);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Remove a post", description = "Deletes a post from the system by its ID. Only accessible to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid post ID supplied"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseDTO<Void> removePost(@Parameter(description = "ID of the post to be deleted") @PathVariable Long postId) {
        adminService.removePost(postId);
        return ResponseDTO.success("Post deleted successfully", null);
    }
}