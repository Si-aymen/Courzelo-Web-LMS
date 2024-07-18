package org.example.courzelo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.time.Instant;

@AllArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND = "User not found : ";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Logging out user");
        User user = userRepository.findUserByEmail(authentication.getName());
        if(user != null){
            log.info("User found");
            user.getActivity().setLastLogout(Instant.now());
            userRepository.save(user);
        }else{
            throw new UsernameNotFoundException(USER_NOT_FOUND + authentication.getName());
        }
        log.info("User logged out");
    }
}