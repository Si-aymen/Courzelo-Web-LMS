package org.example.courzelo.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.RefreshToken;
import org.example.courzelo.repositories.RefreshTokenRepository;
import org.example.courzelo.serviceImpls.UserServiceImpl;
import org.example.courzelo.services.IRefreshTokenService;
import org.example.courzelo.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final Logger jwtLogger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserServiceImpl userDetailsService;
    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private IRefreshTokenService iRefreshTokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${Security.app.jwtExpirationMs}")
    private long jwtExpirationMs;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        log.info("AuthTokenFilter: doFilterInternal "+ request.getRequestURI());
            if (!hasAuthToken(request.getCookies())) {
                log.info("No auth token found in cookies");
                filterChain.doFilter(request, response);
                return;
            }

        String accessToken = cookieUtil.getAccessTokenFromCookies(request);
        String refreshToken = cookieUtil.getRefreshTokenFromCookies(request);
        try {
            if (accessToken != null) {
                handleAccessToken(request, accessToken);
                filterChain.doFilter(request, response);
            } else if (refreshToken != null) {
                handleRefreshToken(response,request, refreshToken);
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            jwtLogger.error("Error during authentication: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
    }

    private boolean hasAuthToken(Cookie[] cookies) {
    return cookies != null &&(Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals("accessToken")) ||
                Arrays.stream(cookies).anyMatch(cookie -> cookie.getName().equals("refreshToken")));
    }

    private void handleAccessToken(HttpServletRequest request, String accessToken) {
        log.info("Handling access token");
        if (jwtUtils.validateJwtToken(accessToken)) {
            String email = jwtUtils.getEmailFromJwtToken(accessToken);
             if (userDetailsService.ValidUser(email)) {
                 log.info("Setting authentication in security context");
                setAuthenticationInSecurityContext(request, userDetailsService.loadUserByEmail(email));
            }
        }
    }

    private void handleRefreshToken(HttpServletResponse response,HttpServletRequest request, String refreshToken) {
        log.info("Handling refresh token");
        if(iRefreshTokenService.ValidToken(refreshToken)) {
            RefreshToken token = refreshTokenRepository.findByToken(refreshToken);
            response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(jwtUtils.generateJwtToken(token.getUser().getEmail()), jwtExpirationMs).toString());
            log.info("Setting authentication in security context");
                setAuthenticationInSecurityContext(request, userDetailsService.loadUserByEmail(token.getUser().getEmail()));
        }
    }

    private void setAuthenticationInSecurityContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

