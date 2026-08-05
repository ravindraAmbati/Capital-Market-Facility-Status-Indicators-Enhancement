package com.company.application.security;

import com.company.application.exception.InvalidTokenException;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Generates, validates, expires, and invalidates API bearer tokens.
 */
@Service
public class TokenService {

    private final TokenProperties properties;
    private final Clock clock;
    private final Map<String, TokenDetails> tokens = new ConcurrentHashMap<>();

    @Autowired
    public TokenService(TokenProperties properties) {
        this(properties, Clock.systemUTC());
    }

    TokenService(TokenProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    public String generateToken(String username, List<String> roles, String origin) {
        String token = UUID.randomUUID().toString().replace("-", "");
        Instant expiresAt = Instant.now(clock).plusSeconds(properties.getExpirationSeconds());
        tokens.put(token, new TokenDetails(username, roles, expiresAt, origin));
        return token;
    }

    public TokenDetails validateToken(String token, String origin) {
        if (!StringUtils.hasText(token)) {
            throw new InvalidTokenException("Missing bearer token");
        }
        TokenDetails details = tokens.get(token);
        if (details == null) {
            throw new InvalidTokenException("Invalid bearer token");
        }
        if (details.getExpiresAt().isBefore(Instant.now(clock))) {
            tokens.remove(token);
            throw new InvalidTokenException("Expired bearer token");
        }
        if (properties.isSameOriginRequired() && !sameOrigin(details.getOrigin(), origin)) {
            throw new InvalidTokenException("Token origin is not allowed");
        }
        return details;
    }

    public void invalidateToken(String token) {
        tokens.remove(token);
    }

    public long expirationSeconds() {
        return properties.getExpirationSeconds();
    }

    private boolean sameOrigin(String expected, String actual) {
        if (!StringUtils.hasText(expected)) {
            return !StringUtils.hasText(actual);
        }
        return expected.equals(actual);
    }
}
