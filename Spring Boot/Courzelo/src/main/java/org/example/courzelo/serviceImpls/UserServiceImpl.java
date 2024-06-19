package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.UserProfileRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserProfile;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;


@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, IUserService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public boolean ValidUser(String email) {
        User user = userRepository.findUserByEmail(email);
        return
                user != null
                && user.isAccountNonLocked()
                && user.isAccountNonExpired()
                && user.isCredentialsNonExpired()
                        && user.isEnabled();
    }

    @Override
    public ResponseEntity<StatusMessageResponse> updateUserProfile(UserProfileRequest userProfileRequest, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        String name = userProfileRequest.getName().toLowerCase();
        String lastName = userProfileRequest.getLastName().toLowerCase();

        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
        if(user.getProfile() == null) {
            user.setProfile(new UserProfile());
        }
        user.getProfile().setName(name);
        user.getProfile().setLastName(lastName);
        user.getProfile().setBirthDate(userProfileRequest.getBirthDate() != null ? userProfileRequest.getBirthDate() : user.getProfile().getBirthDate());
        user.getProfile().setBio(userProfileRequest.getBio() != null ? userProfileRequest.getBio() : user.getProfile().getBio());
        user.getProfile().setTitle(userProfileRequest.getTitle() != null ? userProfileRequest.getTitle() : user.getProfile().getTitle());
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("success", "Profile updated successfully"));
    }



}
