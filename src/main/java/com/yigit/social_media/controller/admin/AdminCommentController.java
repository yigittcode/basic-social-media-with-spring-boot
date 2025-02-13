package com.yigit.social_media.controller.admin;

import org.springframework.web.bind.annotation.*;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("${rest.api.endpoint.prefix}/admin/comments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Comment Controller", description = "Operations related to comment management by admins")
public class AdminCommentController {

    private final AdminService adminService;

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Remove a comment by ID", description = "Allows admins to delete a comment from the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comment successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Comment not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    public ResponseDTO<Void> removeComment(
            @Parameter(description = "ID of the comment to be deleted") @PathVariable Long commentId) {
        adminService.removeComment(commentId);
        return ResponseDTO.success("Comment deleted successfully from the system", null);
    }
}