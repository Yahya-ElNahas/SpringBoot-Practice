package com.practice.test.Infrastructure.Jwt;

import com.practice.test.Dtos.Responses.ErrorResponse;
import com.practice.test.Entities.Session.Session;
import com.practice.test.Repositories.SessionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            jwtService.validateToken(token);

            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);
            int sessionId = jwtService.extractSessionId(token);

            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(Exception::new);
            if(session.isRevoked() || session.getExpiryDate().isBefore(LocalDateTime.now())
            ) {
                throw new Exception();
            }

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            JwtUserPrincipal principal = new JwtUserPrincipal(email, sessionId, authority);

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
            sendTokenError(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void sendTokenError(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse(
                401,
                "Unauthorized",
                "Invalid or expired token",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}