package com.practice.main.authentication.application.util;

import com.practice.main.common.cache.SessionCache;
import com.practice.main.authentication.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionCacheService {

    @Qualifier("sessionRedisTemplate")
    private final RedisTemplate<String, SessionCache> redisTemplate;

    public void cache(Session session) {
        redisTemplate.opsForValue().set(
                "session:" + session.getId(),
                new SessionCache(session.getUserId(), session.getUserRole(), session.getToken()),
                Duration.between(Instant.now(), session.getExpiryDate())
        );
    }

    public SessionCache getBySessionId(String sessionId) {
        return redisTemplate.opsForValue().get("session:" + sessionId);
    }

    public void deleteBySessionIds(List<UUID> sessionIds) {
        redisTemplate.delete(sessionIds.stream()
                .map(id -> "session:" + id.toString()).toList()
        );
    }
}