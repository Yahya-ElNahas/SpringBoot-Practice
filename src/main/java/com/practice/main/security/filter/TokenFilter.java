package com.practice.main.security.filter;

import com.practice.main.authentication.domain.Session;
import com.practice.main.authentication.application.exception.InvalidSessionException;
import com.practice.main.authentication.infrastructure.SessionRepository;
import com.practice.main.security.token.AccessTokenService;
import com.practice.main.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private final AccessTokenService accessTokenService;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = accessTokenService.validateToken(token);

            UUID userId = accessTokenService.extractUserId(claims);
            String role = accessTokenService.extractRole(claims);
            UUID sessionId = accessTokenService.extractSessionId(claims);

            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(InvalidSessionException::new);
            if(session.isRevoked() || session.getExpiryDate().isBefore(Instant.now())
            ) {
                throw new InvalidSessionException();
            }

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            UserPrincipal principal = new UserPrincipal(userId, sessionId, authority);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of(authority)
            );
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();

            log.warn("Token validation failed: {} - {}", e.getClass().getSimpleName(), e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}