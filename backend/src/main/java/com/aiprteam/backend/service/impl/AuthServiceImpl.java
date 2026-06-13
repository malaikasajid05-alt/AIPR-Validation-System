package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.auth.AuthResponseDTO;
import com.aiprteam.backend.dto.auth.LoginRequestDTO;
import com.aiprteam.backend.dto.auth.RegisterRequestDTO;
import com.aiprteam.backend.entity.Users;
import com.aiprteam.backend.mapper.AuthMapper;
import com.aiprteam.backend.repository.UsersRepository;
import com.aiprteam.backend.security.JwtService;
import com.aiprteam.backend.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthMapper authMapper,
            AuthenticationManager authenticationManager) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authMapper = authMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        Users user = authMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Users saved = usersRepository.save(user);
        String token = jwtService.generateToken(saved.getEmail());
        return authMapper.toResponse(token, saved);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Users user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        String token = jwtService.generateToken(user.getEmail());
        return authMapper.toResponse(token, user);
    }
}
