package com.aiprteam.backend.mapper;

import com.aiprteam.backend.dto.auth.AuthResponseDTO;
import com.aiprteam.backend.dto.auth.RegisterRequestDTO;
import com.aiprteam.backend.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public Users toEntity(RegisterRequestDTO request) {
        Users user = new Users();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        return user;
    }

    public AuthResponseDTO toResponse(String token, Users user) {
        return new AuthResponseDTO(token, user.getEmail(), user.getFullName());
    }
}
