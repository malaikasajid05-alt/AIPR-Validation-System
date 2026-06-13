package com.aiprteam.backend.controller;

import com.aiprteam.backend.dto.user.UpdatePasswordRequestDTO;
import com.aiprteam.backend.dto.user.UpdateProfileRequestDTO;
import com.aiprteam.backend.dto.user.UserProfileDTO;
import com.aiprteam.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileDTO getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getProfile(userDetails.getUsername());
    }

    @PutMapping("/me")
    public UserProfileDTO updateProfile(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateProfileRequestDTO request) {
        return userService.updateProfile(userDetails.getUsername(), request);
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdatePasswordRequestDTO request) {
        userService.updatePassword(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
