package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import org.example.courzelo.dto.responses.PaginatedUsersResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.UserResponse;
import org.example.courzelo.models.Role;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.ISuperAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SuperAdminServiceImpl implements ISuperAdminService {
   private final UserRepository userRepository;
    @Override
    public ResponseEntity<PaginatedUsersResponse> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageRequest);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

        PaginatedUsersResponse response = new PaginatedUsersResponse();
        response.setUsers(userResponses);
        response.setCurrentPage(userPage.getNumber());
        response.setTotalPages(userPage.getTotalPages());
        response.setTotalItems(userPage.getTotalElements());
        response.setItemsPerPage(userPage.getSize());

        return ResponseEntity.ok(response);
    }
    @Override
    public ResponseEntity<StatusMessageResponse> toggleUserBanStatus(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(new StatusMessageResponse("Error", "User not found"));
        }
        user.getSecurity().setBan(!user.getSecurity().getBan());
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("Success", user.getEmail()+" ban status toggled"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> toggleUserEnabledStatus(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(new StatusMessageResponse("Error", "User not found"));
        }
        user.getSecurity().setEnabled(!user.getSecurity().isEnabled());
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("Success", user.getEmail()+" enabled status toggled"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> addRoleToUser(String email, String role) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(new StatusMessageResponse("Error", "User not found"));
        }
        if(user.getRoles().contains(Role.valueOf(role))){
            return ResponseEntity.badRequest().body(new StatusMessageResponse("Error", "Role already assigned"));
        }
        user.getRoles().add(Role.valueOf(role));
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("Success", email +" is now assigned role "+role));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> removeRoleFromUser(String email, String role) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(new StatusMessageResponse("Error", "User not found"));
        }
        if(!user.getRoles().contains(Role.valueOf(role))){
            return ResponseEntity.badRequest().body(new StatusMessageResponse("Error", "Role not assigned"));
        }
        user.getRoles().remove(Role.valueOf(role));
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("Success", email +" is no longer assigned role "+role));
    }
}
