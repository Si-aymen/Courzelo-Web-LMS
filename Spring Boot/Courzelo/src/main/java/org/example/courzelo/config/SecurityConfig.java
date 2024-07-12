package org.example.courzelo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf().ignoringRequestMatchers("jobOffer/**")
                .and()
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","jobOffer/**").permitAll() // Allow access to Swagger UI and docs
                        .anyRequest().authenticated() // Require authentication for all other requests
                );
        return http.build();
    }
}
