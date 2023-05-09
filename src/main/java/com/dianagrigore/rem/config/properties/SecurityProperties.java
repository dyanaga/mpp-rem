package com.dianagrigore.rem.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security")
public class SecurityProperties {

    private String issuer;
    private String secret;
    private Integer tokenTtl;
    private Integer refreshTokenTtl;
    private Boolean adminEnabled;
    private String adminUserId;
    private String adminPassword;

    public String getSecret() {
        return secret;
    }

    public SecurityProperties setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public SecurityProperties setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
        return this;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public SecurityProperties setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
        return this;
    }

    public Integer getTokenTtl() {
        return tokenTtl;
    }

    public SecurityProperties setTokenTtl(Integer tokenTtl) {
        this.tokenTtl = tokenTtl;
        return this;
    }

    public Integer getRefreshTokenTtl() {
        return refreshTokenTtl;
    }

    public SecurityProperties setRefreshTokenTtl(Integer refreshTokenTtl) {
        this.refreshTokenTtl = refreshTokenTtl;
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public SecurityProperties setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public Boolean getAdminEnabled() {
        return adminEnabled;
    }

    public SecurityProperties setAdminEnabled(Boolean adminEnabled) {
        this.adminEnabled = adminEnabled;
        return this;
    }
}
