package org.example.courzelo.services;

import org.example.courzelo.dto.requests.UserProfileRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

public interface IUserService {

UserDetails loadUserByEmail(String email);

 boolean ValidUser(String email);

 ResponseEntity<StatusMessageResponse> updateUserProfile(UserProfileRequest userProfileRequest, Principal principal);


}
