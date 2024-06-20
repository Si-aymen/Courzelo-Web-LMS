package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.ProfileInformationRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserProfile;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


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


}
