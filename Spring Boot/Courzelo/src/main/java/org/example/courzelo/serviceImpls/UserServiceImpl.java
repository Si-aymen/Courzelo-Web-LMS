package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.ProfileInformationRequest;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    public ResponseEntity<StatusMessageResponse> updateUserProfile(ProfileInformationRequest profileInformationRequest, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        String name = profileInformationRequest.getName().toLowerCase();
        String lastName = profileInformationRequest.getLastname().toLowerCase();

        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        lastName = Character.toUpperCase(lastName.charAt(0)) + lastName.substring(1);
        if(user.getProfile() == null) {
            user.setProfile(new UserProfile());
        }
        user.getProfile().setName(name);
        user.getProfile().setLastname(lastName);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthDate = formatter.parse(profileInformationRequest.getBirthDate());
            user.getProfile().setBirthDate(profileInformationRequest.getBirthDate() != null ? birthDate: user.getProfile().getBirthDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        user.getProfile().setBio(profileInformationRequest.getBio());
        user.getProfile().setTitle(profileInformationRequest.getTitle());
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("success", "Profile updated successfully"));
    }



}
