package com.yigit.social_media.controller;

import org.springframework.web.bind.annotation.*;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.dto.user.request.UserRequestDTO;
import com.yigit.social_media.dto.user.response.UserResponseDTO;
import com.yigit.social_media.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;


import java.util.List;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    private final IUserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user", description = "Retrieves the currently authenticated user's information.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseDTO<UserResponseDTO> getCurrentUser() {
        UserResponseDTO user = userService.getCurrentUser();
        return ResponseDTO.success("Current user retrieved successfully", user);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseDTO<UserResponseDTO> getUserById(@Parameter(description = "ID of the user to retrieve", required = true) @PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseDTO.success("User retrieved successfully", user);
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Searches users based on a query string.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = List.class, subTypes = {UserResponseDTO.class})))
    public ResponseDTO<List<UserResponseDTO>> searchUsers(@Parameter(description = "Search query string", required = true) @RequestParam String query) {
        List<UserResponseDTO> users = userService.searchUsers(query);
        return ResponseDTO.success("Users retrieved successfully", users);
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update current user", description = "Updates the currently authenticated user's information.")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseDTO<UserResponseDTO> updateUser(@Valid @RequestBody(description = "User object to update", required = true, content = @Content(schema = @Schema(implementation = UserRequestDTO.class))) UserRequestDTO userRequestDTO) {
        UserResponseDTO updatedUser = userService.updateUser(userRequestDTO);
        return ResponseDTO.success("User updated successfully", updatedUser);
    }

    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete current user", description = "Deletes the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseDTO<Void> deleteUser() {
        userService.deleteUser();
        return ResponseDTO.success("User deleted successfully", null);
    }
}