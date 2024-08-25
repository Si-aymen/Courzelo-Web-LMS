package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import org.example.courzelo.dto.responses.*;
import org.example.courzelo.models.Role;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.ISuperAdminService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SuperAdminServiceImpl implements ISuperAdminService {
   private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    @Override
    public ResponseEntity<PaginatedUsersResponse> getAllUsers(int page, int size, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Query query = new Query().with(pageRequest);

        if (keyword != null && !keyword.trim().isEmpty()) {
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("email").regex(keyword, "i"),
                    Criteria.where("profile.lastname").regex(keyword, "i"),
                    Criteria.where("profile.name").regex(keyword, "i"),
                    Criteria.where("profile.title").regex(keyword, "i"),
                    Criteria.where("roles").regex(keyword, "i")

            );
            query.addCriteria(criteria);
        }

        List<User> users = mongoTemplate.find(query, User.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class);

        List<UserResponse> userResponses = users.stream()
                .map(
                        user -> UserResponse.builder()
                                .email(user.getEmail())
                                .profile(new UserProfileResponse(user.getProfile()))
                                .roles(user.getRoles().stream().map(Role::name).toList())
                                .security(new UserSecurityResponse(user.getSecurity()))
                                .build()
                )
                .collect(Collectors.toList());

        PaginatedUsersResponse response = new PaginatedUsersResponse();
        response.setUsers(userResponses);
        response.setCurrentPage(page);
        response.setTotalPages((int) Math.ceil((double) total / size));
        response.setTotalItems(total);
        response.setItemsPerPage(size);

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
