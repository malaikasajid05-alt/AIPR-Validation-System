package com.aiprteam.backend.service;

import com.aiprteam.backend.dto.user.UpdatePasswordRequestDTO;
import com.aiprteam.backend.dto.user.UpdateProfileRequestDTO;
import com.aiprteam.backend.dto.user.UserProfileDTO;

public interface UserService {
    UserProfileDTO getProfile(String email);

    UserProfileDTO updateProfile(String email, UpdateProfileRequestDTO request);

    void updatePassword(String email, UpdatePasswordRequestDTO request);
}
