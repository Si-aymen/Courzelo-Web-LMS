package org.example.courzelo.services;

import org.example.courzelo.dto.requests.ProfileInformationRequest;
import org.example.courzelo.dto.requests.UpdatePasswordRequest;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.dto.responses.QRCodeResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface IUserService {

UserDetails loadUserByEmail(String email);

 boolean ValidUser(String email);

 ResponseEntity<StatusMessageResponse> updateUserProfile(ProfileInformationRequest profileInformationRequest, Principal principal);


    ResponseEntity<StatusMessageResponse> uploadProfileImage(MultipartFile file, Principal principal);

    ResponseEntity<byte[]> getProfileImage(Principal principal, String email);

    ResponseEntity<LoginResponse> getUserProfile(String email);

    ResponseEntity<StatusMessageResponse> updatePassword(UpdatePasswordRequest updatePasswordRequest, Principal principal);
    ResponseEntity<StatusMessageResponse> resetPassword(UpdatePasswordRequest updatePasswordRequest, String code);
    ResponseEntity<QRCodeResponse> generateTwoFactorAuthQrCode(String email);
    ResponseEntity<StatusMessageResponse> enableTwoFactorAuth(String email,String verificationCode);
    ResponseEntity<StatusMessageResponse> disableTwoFactorAuth(String email);
    boolean verifyTwoFactorAuth(String email, int verificationCode);
    ResponseEntity<LoginResponse> getUserProfileByEmail(Principal principal,String email);
}
