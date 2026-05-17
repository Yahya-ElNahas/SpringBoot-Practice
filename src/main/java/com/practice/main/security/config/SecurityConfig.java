package com.practice.main.security.config;

import com.practice.main.security.handler.AccessDeniedHandler;
import com.practice.main.security.handler.UnauthorizedHandler;
import com.practice.main.security.filter.TokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@EnableCaching
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    private final AccessDeniedHandler accessDeniedHandler;
    private final UnauthorizedHandler unauthorizedHandler;

    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/users/**",
                                        "/products/**",
                                        "/order/**",
                                        "/receipts/**",
                                        "/dashboard/**",
                                        "/llm"
                                ).authenticated()

                                .requestMatchers("/actuator/prometheus").permitAll()
                                .requestMatchers("/actuator/**").hasRole("ADMIN")

                                .requestMatchers("/error").permitAll()

                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                                .anyRequest().authenticated()
                )
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                                .authenticationEntryPoint(unauthorizedHandler)
                                .accessDeniedHandler(accessDeniedHandler)
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();

        cors.addAllowedOrigin(FRONTEND_URL);

        cors.setAllowedMethods(
                List.of("GET", "POST", "PATCH", "DELETE")
        );

        cors.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept-Language")
        );

        cors.setExposedHeaders(
                List.of("X-Request-Id")
        );

        cors.setAllowCredentials(true);

        cors.setMaxAge(60L * 60L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);

        return source;
    }
}