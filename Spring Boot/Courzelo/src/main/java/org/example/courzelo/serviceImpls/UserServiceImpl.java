package org.example.courzelo.serviceImpls;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.ProfileInformationRequest;
import org.example.courzelo.dto.requests.UpdatePasswordRequest;
import org.example.courzelo.dto.requests.UserProfileRequest;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.dto.responses.QRCodeResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.UserResponse;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserProfile;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;


@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService, IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository,@Lazy PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }


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
        log.info("Updating profile for user: " + principal.getName());
        log.info("Profile information: " + profileInformationRequest.toString());
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
            Date birthDate = null;
            if(profileInformationRequest.getBirthDate() != null)
            {
              birthDate = formatter.parse(profileInformationRequest.getBirthDate());
            }
            user.getProfile().setBirthDate(birthDate != null ? birthDate: user.getProfile().getBirthDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        user.getProfile().setBio(profileInformationRequest.getBio());
        user.getProfile().setTitle(profileInformationRequest.getTitle());
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("success", "Profile updated successfully"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> uploadProfileImage(MultipartFile file, Principal principal) {
        try {
            // Define the path where you want to save the image
            String baseDir = "upload" + File.separator + principal.getName() + File.separator + "profile-image" + File.separator;

            // Create the directory if it doesn't exist
            File dir = new File(baseDir);
            if (!dir.exists()) {
                boolean dirsCreated = dir.mkdirs();
                if (!dirsCreated) {
                    throw new IOException("Failed to create directories");
                }
            }

            // Get the original file name
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            // Generate a random filename
            String newFileName = UUID.randomUUID() + extension;
            // Define the path to the new file
            String filePath = baseDir + newFileName;
            log.info("File path: " + filePath);
            Files.copy(file.getInputStream(), new File(filePath).toPath());
            // Save the file to the server
            //file.transferTo(new File(filePath));

            // Get the user
            User user = userRepository.findUserByEmail(principal.getName());
            //delete old image
            if(user.getProfile().getProfileImage() != null)
            {
                File oldImage = new File(user.getProfile().getProfileImage());
                if(oldImage.exists())
                {
                    oldImage.delete();
                }
            }
            // Save the file path and name in the user's profile
            user.getProfile().setProfileImage(filePath);
            // Save the user
            userRepository.save(user);

            return ResponseEntity.ok(new StatusMessageResponse("success", "Profile image uploaded successfully"));
        } catch (Exception e) {
            log.error("Error uploading image: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusMessageResponse("error", "Could not upload the image. Please try again."));
        }
    }

    @Override
    public ResponseEntity<byte[]> getProfileImage(Principal principal) {
        try {
            // Get the user
            User user = userRepository.findUserByEmail(principal.getName());
            // Get the file path
            String filePath = user.getProfile().getProfileImage();
            // Read the file
            byte[] image = Files.readAllBytes(new File(filePath).toPath());
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            log.error("Error getting image: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<LoginResponse> getUserProfile(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse("error", "User not found"));
        }
        return ResponseEntity.ok(new LoginResponse("success", "User profile retrieved successfully", new UserResponse(user)));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> updatePassword(UpdatePasswordRequest updatePasswordRequest, Principal principal) {
        log.info("Updating password for user: " + principal.getName());
        log.info("Password information: " + updatePasswordRequest.toString());
        User user = userRepository.findUserByEmail(principal.getName());
        if (!encoder.matches(updatePasswordRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusMessageResponse("error", "Incorrect password"));
        }
        user.setPassword(encoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("success", "Password updated successfully"));
    }
    @Override
    public ResponseEntity<QRCodeResponse> generateTwoFactorAuthQrCode(String email) {
        User user = userRepository.findUserByEmail(email);
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();

        user.getSecurity().setTwoFactorAuthKey(key.getKey());
        userRepository.save(user);

        String qrCodeData = "otpauth://totp/" + email + "?secret=" + key.getKey() + "&issuer=Courzelo";
        byte[] qrCodeImage;
        try {
            qrCodeImage = generateQRCodeImage(qrCodeData, 200, 200);
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Could not generate QR code", e);
        }
        String qrCodeImageBase64 = Base64.getEncoder().encodeToString(qrCodeImage);
        return ResponseEntity.ok(new QRCodeResponse("success", "QR code generated successfully", qrCodeImageBase64));
    }
    public byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    @Override
    public ResponseEntity<StatusMessageResponse> enableTwoFactorAuth(String email,String verificationCode){
        User user = userRepository.findUserByEmail(email);
        if (verificationCode.matches("\\d+") && verifyTwoFactorAuth(email, Integer.parseInt(verificationCode))) {
            user.getSecurity().setTwoFactorAuthEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok(new StatusMessageResponse("success", "Two-factor authentication enabled successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusMessageResponse("error", "Invalid verification code"));
    }
    @Override
    public ResponseEntity<StatusMessageResponse> disableTwoFactorAuth(String email){
        User user = userRepository.findUserByEmail(email);
        user.getSecurity().setTwoFactorAuthKey(null);
        user.getSecurity().setTwoFactorAuthEnabled(false);
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("success", "Two-factor authentication disabled successfully"));
    }
    @Override
    public boolean verifyTwoFactorAuth(String email, int verificationCode) {
        log.info("Starting TFA verification for user: {}", email);
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
            log.warn("User not found: {}", email);
            return false;
        }
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean isCodeValid = gAuth.authorize(user.getSecurity().getTwoFactorAuthKey(), verificationCode);
        if(isCodeValid) {
            log.info("TFA code verified for user: {}", email);
        } else {
            log.warn("Invalid TFA code {} for user: {}", verificationCode ,email);
        }
        return isCodeValid;
    }


}
