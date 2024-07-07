package org.example.courzelo.services;

import org.example.courzelo.dto.responses.PaginatedUsersResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.springframework.http.ResponseEntity;

public interface ISuperAdminService {
   ResponseEntity<PaginatedUsersResponse> getAllUsers(int page, int size);
   ResponseEntity<StatusMessageResponse> toggleUserBanStatus(String email);
   ResponseEntity<StatusMessageResponse> toggleUserEnabledStatus(String email);
   ResponseEntity<StatusMessageResponse> addRoleToUser(String email, String role);
    ResponseEntity<StatusMessageResponse> removeRoleFromUser(String email, String role);
}
