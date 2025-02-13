package com.yigit.social_media.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yigit.social_media.dto.auth.request.RegisterRequestDTO;
import com.yigit.social_media.dto.auth.request.LoginRequestDTO;
import com.yigit.social_media.dto.auth.response.RegisterResponseDTO;
import com.yigit.social_media.dto.auth.response.LoginResponseDTO;
import com.yigit.social_media.model.User;
import com.yigit.social_media.repository.UserRepository;
import com.yigit.social_media.security.jwt.JwtService;
import com.yigit.social_media.security.user.PrincipalUser;
import com.yigit.social_media.enums.RoleTypes;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(RoleTypes.CASUAL);
        
        userRepository.save(user);
        
        String token = jwtService.generateToken(new PrincipalUser(user));
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setToken(token);
        response.setExpiresIn(jwtService.getExpirationTime());
        return response;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword()
            )
        );
        
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        String token = jwtService.generateToken(new PrincipalUser(user));
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setExpiresIn(jwtService.getExpirationTime());
        return response;
    }
}
