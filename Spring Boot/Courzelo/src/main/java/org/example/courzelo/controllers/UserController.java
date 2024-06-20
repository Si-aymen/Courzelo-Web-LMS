package org.example.courzelo.controllers;

import lombok.AllArgsConstructor;
import org.example.courzelo.dto.requests.ProfileInformationRequest;
import org.example.courzelo.dto.requests.UserProfileRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

}
