package org.example.courzelo.serviceImpls;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.LoginRequest;
import org.example.courzelo.dto.requests.SignupRequest;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.UserResponse;
import org.example.courzelo.models.Role;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.security.jwt.JWTUtils;
import org.example.courzelo.services.IAuthService;
import org.example.courzelo.services.IRefreshTokenService;
import org.example.courzelo.services.IUserService;
import org.example.courzelo.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {
    public static final String USER_NOT_FOUND = "User not found : ";
    private final UserRepository userRepository;
    private final IRefreshTokenService iRefreshTokenService;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    @Value("${Security.app.jwtExpirationMs}")
    private long jwtExpirationMs;
    @Value("${Security.app.refreshExpirationMs}")
    private long refreshExpirationMs;
    @Value("${Security.app.refreshRememberMeExpirationMs}")
    private long refreshRememberMeExpirationMs;

    public AuthServiceImpl(UserRepository userRepository, IRefreshTokenService iRefreshTokenService, JWTUtils jwtUtils, PasswordEncoder encoder, CookieUtil cookieUtil, AuthenticationManager authenticationManager, IUserService userService) {
        this.userRepository = userRepository;
        this.iRefreshTokenService = iRefreshTokenService;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
        this.cookieUtil = cookieUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<StatusMessageResponse> logout(String email, HttpServletRequest request, HttpServletResponse response) {
        log.info("Logging out user");
        User user = userRepository.findUserByEmail(email);
        if(user != null){
            log.info("User found");
            user.getActivity().setLastLogout(Instant.now());
            userRepository.save(user);
            new SecurityContextLogoutHandler().logout(request, response, null);
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie("accessToken", 0L).toString());
            log.info("Logout: Access Token removed");
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie("refreshToken", 0L).toString());
            log.info("Logout :Refresh Token removed");
            log.info("User logged out");
            return ResponseEntity.ok(new StatusMessageResponse("success","User logged out successfully"));
        }else{
            throw new UsernameNotFoundException(USER_NOT_FOUND + email);
        }
    }
    boolean isUserAuthenticated(){
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                    String username = userDetails.getUsername();
                    log.info("Authenticated user's username: " + username);
                    return true;
                }
                return false;
            }
            log.info("User not authenticated");
            return false;
        }
        return false;
    }
    @Override
    public ResponseEntity<LoginResponse> authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {
        log.info("Authenticating user");
        if(isUserAuthenticated()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse("error", "User already authenticated"));
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User userDetails = (User) authentication.getPrincipal();
            if(userDetails.getSecurity().isTwoFactorAuthEnabled())
            {
                log.info("Two factor authentication enabled");
                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse("succes","TFA code required",true));
            }
            setHeaders(response,userDetails);
            userDetails.getActivity().setLastLogin(Instant.now());
            userDetails.getSecurity().setRememberMe(loginRequest.isRememberMe());
            userRepository.save(userDetails);
            log.info("User authenticated successfully");
            return ResponseEntity.ok(new LoginResponse("success","Login successful", new UserResponse(userDetails) ));
        } catch (DisabledException e) {
            log.error("User not verified");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse("error","Please verify your email first"));
        } catch (LockedException e) {
            log.error("User account locked");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse("error","Account locked"));
        } catch (AuthenticationException e) {
            log.error("Invalid email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("error","Invalid email or password"));
        }
    }

    void setHeaders(HttpServletResponse response,User userDetails){
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(jwtUtils.generateJwtToken(userDetails.getEmail()), jwtExpirationMs).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(
                iRefreshTokenService.createRefreshToken(userDetails.getEmail(), userDetails.getSecurity().isRememberMe() ? refreshRememberMeExpirationMs : refreshExpirationMs).getToken()
                , userDetails.getSecurity().isRememberMe() ? refreshRememberMeExpirationMs : refreshExpirationMs).toString());
    }

    @Override
    public ResponseEntity<StatusMessageResponse> saveUser(SignupRequest signupRequest) {
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StatusMessageResponse("error","Email already in use"));
        }
        User user = new User(
                signupRequest.getEmail().toLowerCase(),
                encoder.encode(signupRequest.getPassword()),
                Role.STUDENT
        );
        userRepository.save(user);
        return ResponseEntity.ok(new StatusMessageResponse("success","User registered successfully"));
    }

    @Override
    public ResponseEntity<LoginResponse> twoFactorAuthentication(String code, LoginRequest loginRequest, HttpServletResponse response) {
        log.info("Authenticating user");
        if(isUserAuthenticated()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse("error", "User already authenticated"));
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail().toLowerCase(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User userDetails = (User) authentication.getPrincipal();
            if(!userService.verifyTwoFactorAuth(userDetails.getEmail(),Integer.parseInt(code)))
            {
                log.error("Invalid two factor authentication code");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("error","Invalid two factor authentication code"));
            }
            setHeaders(response,userDetails);
            userDetails.getActivity().setLastLogin(Instant.now());
            userDetails.getSecurity().setRememberMe(loginRequest.isRememberMe());
            userRepository.save(userDetails);
            log.info("User authenticated successfully");
            return ResponseEntity.ok(new LoginResponse("success","Login successful", new UserResponse(userDetails) ));
        } catch (DisabledException e) {
            log.error("User not verified");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse("error","Please verify your email first"));
        } catch (LockedException e) {
            log.error("User account locked");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoginResponse("error","Account locked"));
        } catch (AuthenticationException e) {
            log.error("Invalid email or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("error","Invalid email or password"));
        }
    }
}
