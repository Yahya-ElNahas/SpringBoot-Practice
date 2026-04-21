package com.practice.test.Infrastructure.Jwt;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@NullMarked
public record JwtUserPrincipal(

        UUID userId,

        UUID sessionId,

        GrantedAuthority authority
) {}