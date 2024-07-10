package org.example.courzelo.controllers;

import lombok.AllArgsConstructor;
import org.example.courzelo.dto.requests.ProfileInformationRequest;
import org.example.courzelo.dto.requests.UpdatePasswordRequest;
import org.example.courzelo.dto.requests.UserProfileRequest;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.dto.responses.QRCodeResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final IUserService userService;

    @PostMapping("/profile")
    public ResponseEntity<StatusMessageResponse> updateUserProfile(@RequestBody ProfileInformationRequest profileInformationRequest, Principal principal  ) {
    return userService.updateUserProfile(profileInformationRequest, principal);
    }
    @PostMapping("/image")
    public ResponseEntity<StatusMessageResponse> uploadProfileImage(@RequestParam("file") MultipartFile file, Principal principal) {
        return userService.uploadProfileImage(file, principal);
    }
    @GetMapping("/image")
    public ResponseEntity<byte[]> getProfileImage(Principal principal) {
        return userService.getProfileImage(principal);
    }
    @GetMapping("/profile")
    public ResponseEntity<LoginResponse> getUserProfile(Principal principal) {
        return userService.getUserProfile(principal.getName());
    }
    @PostMapping("/updatePassword")
    public ResponseEntity<StatusMessageResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest , Principal principal) {
        return userService.updatePassword(updatePasswordRequest, principal);
    }
    @GetMapping("/qrCode")
    public ResponseEntity<QRCodeResponse> generateTwoFactorAuthQrCode(Principal principal) {
        return userService.generateTwoFactorAuthQrCode(principal.getName());
    }
    @PostMapping("/enableTwoFactorAuth")
    public ResponseEntity<StatusMessageResponse> enableTwoFactorAuth(@RequestParam String verificationCode,Principal principal) {
        return userService.enableTwoFactorAuth(principal.getName(), verificationCode);
    }
    @DeleteMapping("/disableTwoFactorAuth")
    public ResponseEntity<StatusMessageResponse> disableTwoFactorAuth(Principal principal) {
     return   userService.disableTwoFactorAuth(principal.getName());
    }

}
