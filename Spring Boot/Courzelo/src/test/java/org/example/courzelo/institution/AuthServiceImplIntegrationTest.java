package org.example.courzelo.institution;

import org.example.courzelo.dto.requests.LoginRequest;
import org.example.courzelo.dto.requests.SignupRequest;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.models.CodeType;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.CourseRepository;
import org.example.courzelo.repositories.GroupRepository;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.security.jwt.JWTUtils;
import org.example.courzelo.serviceImpls.AuthServiceImpl;
import org.example.courzelo.services.ICodeVerificationService;
import org.example.courzelo.services.IMailService;
import org.example.courzelo.services.IRefreshTokenService;
import org.example.courzelo.services.IUserService;
import org.example.courzelo.utils.CookieUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.Principal;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthServiceImplIntegrationTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IRefreshTokenService refreshTokenService;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private IUserService userService;

    @Mock
    private IMailService mailService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testSaveUser_Success() {
        //delete if exist
        if(userRepository.existsByEmail("newuser@example.com")) {
            userRepository.deleteByEmail("newuser@example.com");
        }
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        // Act
        ResponseEntity<StatusMessageResponse> response = authService.saveUser(signupRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody().getMessage());
    }

    @Test
    public void testCheckAuth_Authenticated() {
        // Arrange
        Principal principal = () -> "user@example.com";
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findUserByEmail(principal.getName())).thenReturn(user);
        // Act
        ResponseEntity<LoginResponse> response = authService.checkAuth(principal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
    }


}
