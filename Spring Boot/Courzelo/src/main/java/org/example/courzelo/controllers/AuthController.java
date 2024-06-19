package org.example.courzelo.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.example.courzelo.dto.requests.LoginRequest;
import org.example.courzelo.dto.requests.SignupRequest;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.services.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@PreAuthorize("permitAll()")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
public class AuthController {
    private final IAuthService authService;
    @PostMapping("/login")
    ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest, @NonNull HttpServletResponse response) {
        return authService.authenticateUser(loginRequest, response);
    }
    @PostMapping("/signup")
    ResponseEntity<StatusMessageResponse> saveUser(@RequestBody SignupRequest signupRequest) {
        return authService.saveUser(signupRequest);
    }
    @GetMapping("/logout")
    void logout(Principal principal) {
        authService.logout(principal.getName());
    }


}
