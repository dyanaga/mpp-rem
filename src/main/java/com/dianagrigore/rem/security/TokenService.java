package com.dianagrigore.rem.security;

import com.dianagrigore.rem.config.properties.SecurityProperties;
import com.dianagrigore.rem.dto.token.TokenDto;
import com.dianagrigore.rem.exception.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private static final String REFRESH = "refresh";
    private static final String AUTH = "auth";

    private final SecurityProperties securityProperties;

    public TokenService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public TokenDto generateToken(String loginId, String type) {

        return new TokenDto()
                .setToken(createAccessToken(loginId, type))
                .setRefreshToken(createRefreshToken(loginId, type))
                .setTimeout(securityProperties.getTokenTtl());
    }

    public Claims validateAccessToken(String jwtToken) {
        Claims body = Jwts.parser().setSigningKey(securityProperties.getSecret()).parseClaimsJws(jwtToken).getBody();
        String scope = (String) body.get("scope");
        if (!AUTH.equals(scope)) {
            throw new BaseException("This is not an access token!");
        }
        return body;
    }

    public Claims validateRefreshToken(String jwtToken) {
        Claims body = Jwts.parser().setSigningKey(securityProperties.getSecret()).parseClaimsJws(jwtToken).getBody();
        String scope = (String) body.get("scope");
        if (!REFRESH.equals(scope)) {
            throw new BaseException("This is not an access token!");
        }
        return body;
    }

    private String createAccessToken(String loginId, String type) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(loginId)
                .claim("authorities", List.of(type))
                .claim("scope", AUTH)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (securityProperties.getTokenTtl() * 1000)))
                .setIssuer(securityProperties.getIssuer())
                .signWith(SignatureAlgorithm.HS256, securityProperties.getSecret())
                .compact();
    }

    private String createRefreshToken(String loginId, String type) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(loginId)
                .claim("scope", REFRESH)
                .claim("authorities", List.of(type))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (securityProperties.getRefreshTokenTtl() * 1000)))
                .setIssuer(securityProperties.getIssuer())
                .signWith(SignatureAlgorithm.HS256, securityProperties.getSecret())
                .compact();
    }

}
