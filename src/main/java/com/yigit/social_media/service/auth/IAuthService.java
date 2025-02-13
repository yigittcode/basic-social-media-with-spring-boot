package com.yigit.social_media.service.auth;

import com.yigit.social_media.dto.auth.request.RegisterRequestDTO;
import com.yigit.social_media.dto.auth.request.LoginRequestDTO;
import com.yigit.social_media.dto.auth.response.RegisterResponseDTO;
import com.yigit.social_media.dto.auth.response.LoginResponseDTO;

public interface IAuthService {
    RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
