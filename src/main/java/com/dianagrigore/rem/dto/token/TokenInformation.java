package com.dianagrigore.rem.dto.token;

public class TokenInformation {
    private String userId;
    private Long timeout;

    public String getUserId() {
        return userId;
    }

    public TokenInformation setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Long getTimeout() {
        return timeout;
    }

    public TokenInformation setTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"TokenInformation\", " +
                "\"userId\":" + (userId == null ? "null" : "\"" + userId + "\"") + ", " +
                "\"timeout\":" + (timeout == null ? "null" : "\"" + timeout + "\"") +
                "}";
    }
}
