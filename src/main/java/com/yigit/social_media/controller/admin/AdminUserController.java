package com.yigit.social_media.controller.admin;

import org.springframework.web.bind.annotation.*;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.dto.user.response.AdminUserResponseDTO;
import com.yigit.social_media.service.admin.AdminService;
import com.yigit.social_media.enums.RoleTypes;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Users", description = "Operations related to user management by administrators")
public class AdminUserController {

    private final AdminService adminService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all users, optionally filtered by role. Only accessible to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    })
    public ResponseDTO<List<AdminUserResponseDTO>> getAllUsers(
            @Parameter(description = "Role to filter users by (optional)") @RequestParam(required = false) RoleTypes role) {
        List<AdminUserResponseDTO> users = adminService.getAllUsers(role);
        return ResponseDTO.success("Users retrieved successfully", users);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Remove a user", description = "Deletes a user from the system by their ID. Only accessible to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseDTO<Void> removeUser(@Parameter(description = "ID of the user to be deleted") @PathVariable Long userId) {
        adminService.removeUser(userId);
        return ResponseDTO.success("User deleted successfully", null);
    }

    @PatchMapping("/{userId}/role")
    @Operation(summary = "Update user role", description = "Updates the role of a specific user. Only accessible to administrators.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or role supplied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseDTO<AdminUserResponseDTO> updateUserRole(
            @Parameter(description = "ID of the user to be updated") @PathVariable Long userId,
            @Parameter(description = "New role for the user") @RequestParam RoleTypes role) {
        AdminUserResponseDTO updatedUser = adminService.updateUserRoles(userId, role);
        return ResponseDTO.success("User role updated successfully", updatedUser);
    }
}