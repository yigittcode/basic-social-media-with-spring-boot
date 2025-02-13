package com.yigit.social_media.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;

import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.dto.comment.request.CommentRequestDTO;
import com.yigit.social_media.dto.comment.response.CommentResponseDTO;
import com.yigit.social_media.service.comment.ICommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Operations related to comments")
public class CommentController {

    private final ICommentService commentService;

    @PostMapping("/post/{postId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a comment on a post", description = "Allows authenticated users to create a comment on a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseDTO<CommentResponseDTO> createComment(
            @Parameter(description = "ID of the post to comment on") @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO comment = commentService.createComment(postId, commentRequestDTO);
        return ResponseDTO.success("Comment created successfully", comment);
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get comments for a post", description = "Retrieves all comments associated with a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseDTO<List<CommentResponseDTO>> getPostComments(@Parameter(description = "ID of the post to retrieve comments from") @PathVariable Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseDTO.success("Comments retrieved successfully", comments);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get comments by a user", description = "Retrieves all comments made by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseDTO<List<CommentResponseDTO>> getUserComments(@Parameter(description = "ID of the user to retrieve comments from") @PathVariable Long userId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByUser(userId);
        return ResponseDTO.success("User's comments retrieved successfully", comments);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my comments", description = "Retrieves all comments made by the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Your comments retrieved successfully")
    })
    public ResponseDTO<List<CommentResponseDTO>> getMyComments() {
        List<CommentResponseDTO> comments = commentService.getMyComments();
        return ResponseDTO.success("Your comments retrieved successfully", comments);
    }

    @PatchMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update a comment", description = "Allows authenticated users to update their own comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to update this comment")
    })
    public ResponseDTO<CommentResponseDTO> updateComment(
            @Parameter(description = "ID of the comment to update") @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO updatedComment = commentService.updateComment(commentId, commentRequestDTO);
        return ResponseDTO.success("Comment updated successfully", updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete a comment", description = "Allows authenticated users to delete their own comment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to delete this comment")
    })
    public ResponseDTO<Void> deleteComment(@Parameter(description = "ID of the comment to delete") @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseDTO.success("Comment deleted successfully", null);
    }
}