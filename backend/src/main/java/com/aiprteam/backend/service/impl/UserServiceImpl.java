package com.aiprteam.backend.service.impl;

import com.aiprteam.backend.dto.user.UpdatePasswordRequestDTO;
import com.aiprteam.backend.dto.user.UpdateProfileRequestDTO;
import com.aiprteam.backend.dto.user.UserProfileDTO;
import com.aiprteam.backend.entity.Users;
import com.aiprteam.backend.exception.ResourceNotFoundException;
import com.aiprteam.backend.repository.UsersRepository;
import com.aiprteam.backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserProfileDTO getProfile(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toDto(user);
    }

    @Override
    @Transactional
    public UserProfileDTO updateProfile(String email, UpdateProfileRequestDTO request) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFullName(request.getFullName().trim());
        return toDto(usersRepository.save(user));
    }

    @Override
    @Transactional
    public void updatePassword(String email, UpdatePasswordRequestDTO request) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }

    private UserProfileDTO toDto(Users user) {
        return new UserProfileDTO(user.getId(), user.getEmail(), user.getFullName());
    }
}
