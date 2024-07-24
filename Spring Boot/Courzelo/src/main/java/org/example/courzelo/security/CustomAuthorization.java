package org.example.courzelo.security;

import lombok.AllArgsConstructor;
import org.example.courzelo.models.Institution;
import org.example.courzelo.models.Role;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomAuthorization {
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;

    public boolean canAccessInstitution(String institutionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userRepository.findUserByEmail(userEmail);
        if(user != null && user.getRoles().contains(Role.SUPERADMIN)){
            return true;
        }
        if (user == null || !user.getRoles().contains(Role.ADMIN)) {
            return false;
        }

        Institution institution = institutionRepository.findById(institutionId).orElse(null);
        if (institution == null) {
            return false;
        }

        return institution.getAdmins().stream().anyMatch(admin -> admin.getEmail().equals(userEmail));
    }
}
