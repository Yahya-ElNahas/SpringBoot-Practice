package com.practice.test.Infrastructure.Jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public class JwtUserPrincipal {

    private String email;
    private int sessionId;
    private GrantedAuthority authority;
}
