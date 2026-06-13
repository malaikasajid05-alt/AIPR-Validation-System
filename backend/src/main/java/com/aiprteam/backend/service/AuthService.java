package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.auth.AuthResponseDTO;
import com.aiprteam.backend.dto.auth.LoginRequestDTO;
import com.aiprteam.backend.dto.auth.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}
