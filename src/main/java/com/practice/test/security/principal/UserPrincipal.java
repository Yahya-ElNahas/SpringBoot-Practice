package com.practice.test.security.principal;

import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

public record UserPrincipal(

        UUID userId,

        UUID sessionId,

        GrantedAuthority authority
) {}