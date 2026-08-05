package com.company.application.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Token behavior properties.
 */
@ConfigurationProperties(prefix = "security.token")
public class TokenProperties {

    private long expirationSeconds = 1800;
    private boolean sameOriginRequired;

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public void setExpirationSeconds(long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }

    public boolean isSameOriginRequired() {
        return sameOriginRequired;
    }

    public void setSameOriginRequired(boolean sameOriginRequired) {
        this.sameOriginRequired = sameOriginRequired;
    }
}
