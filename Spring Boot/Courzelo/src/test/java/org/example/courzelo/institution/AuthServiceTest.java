package org.example.courzelo.institution;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.courzelo.dto.requests.LoginRequest;
import org.example.courzelo.dto.requests.SignupRequest;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.models.RefreshToken;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserEducation;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.security.jwt.JWTUtils;
import org.example.courzelo.serviceImpls.AuthServiceImpl;
import org.example.courzelo.services.IRefreshTokenService;
import org.example.courzelo.utils.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;



import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private CookieUtil cookieUtil;
    @Mock
    private IRefreshTokenService iRefreshTokenService;
    @Mock
    private JWTUtils jwtUtils;
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_EmailAlreadyExists_ReturnsConflict() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // Act
        ResponseEntity<StatusMessageResponse> response = authService.saveUser(signupRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Email already in use", response.getBody().getMessage());
    }

    @Test
    void saveUser_SuccessfulRegistration_ReturnsSuccess() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(encoder.encode(any())).thenReturn("encodedPassword");

        // Act
        ResponseEntity<StatusMessageResponse> response = authService.saveUser(signupRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("User registered successfully", response.getBody().getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void authenticateUser_InvalidCredentials_ReturnsBadRequest() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Invalid credentials") {});

        // Act
        ResponseEntity<LoginResponse> response = authService.authenticateUser(loginRequest, mock(HttpServletResponse.class));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Invalid email or password", response.getBody().getMessage());
    }

    @Test
    void authenticateUser_SuccessfulAuthentication_ReturnsSuccess() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        loginRequest.setRememberMe(true);
        User userDetails = new User();
        userDetails.setEmail("test@example.com");
        userDetails.setEducation(new UserEducation());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(iRefreshTokenService.createRefreshToken(anyString(), anyLong())).thenReturn(RefreshToken.builder()
                        .id("mockId")
                .build()); // Mock the refresh token service
        when(jwtUtils.generateJwtToken(anyString())).thenReturn("mockJwtToken");
        // Mock the ResponseCookie objects
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "mockAccessToken")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "mockRefreshToken")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24)
                .build();

        when(cookieUtil.createAccessTokenCookie(anyString(), anyLong())).thenReturn(accessTokenCookie);
        when(iRefreshTokenService.createRefreshToken(anyString(), anyLong())).thenReturn(RefreshToken.builder()
                .id("mockId")
                        .token("mockRefreshToken")
                .build());
        when(cookieUtil.createRefreshTokenCookie(anyString(), anyLong())).thenReturn(refreshTokenCookie);

        HttpServletResponse response = mock(HttpServletResponse.class);

        // Act
        ResponseEntity<LoginResponse> authResponse = authService.authenticateUser(loginRequest, response);

        // Assert
        assertEquals(HttpStatus.OK, authResponse.getStatusCode());
        assertEquals("success", authResponse.getBody().getStatus());
        verify(userRepository, times(1)).save(userDetails);
    }


    @Test
    void checkAuth_UserIsAuthenticated_ReturnsUserDetails() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setEducation(new UserEducation());
        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);
        when(authService.isUserAuthenticated()).thenReturn(true);

        // Act
        ResponseEntity<LoginResponse> response = authService.checkAuth(principal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Login successful", response.getBody().getMessage());
        assertEquals("test@example.com", response.getBody().getUser().getEmail());
    }

    @Test
    void checkAuth_UserIsNotAuthenticated_ReturnsError() {
        // Arrange
        when(authService.isUserAuthenticated()).thenReturn(false);

        // Act
        ResponseEntity<LoginResponse> response = authService.checkAuth(principal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("User not authenticated", response.getBody().getMessage());
    }
}
