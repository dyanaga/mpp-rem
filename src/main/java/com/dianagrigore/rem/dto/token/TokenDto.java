package com.dianagrigore.rem.dto.token;

public class TokenDto {
    public String token;
    public String refreshToken;
    public Integer timeout;

    public String getToken() {
        return token;
    }

    public TokenDto setToken(String token) {
        this.token = token;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public TokenDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public TokenDto setTimeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    @Override
    public String toString() {
        return "{\"_class\":\"TokenDto\", " +
                "\"token\":" + (token == null ? "null" : "\"" + token + "\"") + ", " +
                "\"refreshToken\":" + (refreshToken == null ? "null" : "\"" + refreshToken + "\"") + ", " +
                "\"timeout\":" + (timeout == null ? "null" : "\"" + timeout + "\"") +
                "}";
    }
}
