package com.yigit.social_media.controller;

import org.springframework.web.bind.annotation.*;
import com.yigit.social_media.dto.common.ResponseDTO;
import com.yigit.social_media.dto.auth.request.LoginRequestDTO;
import com.yigit.social_media.dto.auth.request.RegisterRequestDTO;
import com.yigit.social_media.dto.auth.response.LoginResponseDTO;
import com.yigit.social_media.dto.auth.response.RegisterResponseDTO;
import com.yigit.social_media.service.auth.IAuthService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("${rest.api.endpoint.prefix}/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management endpoints")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account and returns authentication token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully registered",
                content = @Content(schema = @Schema(implementation = RegisterResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    public ResponseDTO<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.register(request);
        return ResponseDTO.success("User registered successfully", response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates user credentials and returns JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseDTO<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseDTO.success("User logged in successfully", response);
    }
} 