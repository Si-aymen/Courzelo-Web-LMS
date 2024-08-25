package org.example.courzelo.controllers;

import lombok.AllArgsConstructor;
import org.example.courzelo.dto.responses.PaginatedUsersResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.services.ISuperAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin")
@AllArgsConstructor
@PreAuthorize("hasRole('SUPERADMIN')")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
public class SuperAdminController {
    private final ISuperAdminService superAdminService;
    @GetMapping("/users")
    public ResponseEntity<PaginatedUsersResponse> getUsersPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword)
    {
        return superAdminService.getAllUsers(page, size,keyword);
    }
    @GetMapping("/toggle-user-ban")
    public ResponseEntity<StatusMessageResponse> toggleUserBanStatus(@RequestParam String email) {
        return superAdminService.toggleUserBanStatus(email);
    }
    @GetMapping("/toggle-user-enabled")
    public ResponseEntity<StatusMessageResponse> toggleUserEnabledStatus(@RequestParam String email) {
        return superAdminService.toggleUserEnabledStatus(email);
    }
    @GetMapping("/add-role")
    public ResponseEntity<StatusMessageResponse> addRoleToUser(@RequestParam String email, @RequestParam String role) {
        return superAdminService.addRoleToUser(email, role);
    }
    @GetMapping("/remove-role")
    public ResponseEntity<StatusMessageResponse> removeRoleFromUser(@RequestParam String email, @RequestParam String role) {
        return superAdminService.removeRoleFromUser(email, role);
    }


}
