package com.dianagrigore.rem.security;

import com.dianagrigore.rem.config.properties.SecurityProperties;
import com.dianagrigore.rem.dto.token.AuthenticationType;
import com.dianagrigore.rem.dto.token.TokenInformation;
import com.dianagrigore.rem.exception.BaseException;
import com.dianagrigore.rem.exception.PermissionDeniedException;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private final UserRepository userJpaRepository;

    private final SecurityProperties securityProperties;

    public SecurityService(UserRepository userRepository, SecurityProperties securityProperties) {
        this.userJpaRepository = userRepository;
        this.securityProperties = securityProperties;
    }

    public void enforcePermission(List<UserType> hasAny, String signature) {
        String userId = getUserId();
        if (securityProperties.getAdminUserId().equals(userId)) {
            logger.debug("Resource with signature [{}] was accessed by default admin.", signature);
            return;
        }
        if (Objects.nonNull(userId)) {
            Optional<User> maybeUser = userJpaRepository.findById(userId);
            if (maybeUser.isPresent()) {
                User user = maybeUser.get();
                if (UserType.ADMIN.equals(user.getType()) || hasAny.contains(user.getType())) {
                    return;
                }
                logger.warn("Resource with signature [{}] was denied for [{}] because does not have all the required permissions.",
                        signature, userId);
            }
        } else {
            logger.warn("Resource with signature [{}] was denied for unknown user.", signature);
        }
        throw new PermissionDeniedException("You don't have access to this data.");
    }

    public String getUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public AuthenticationType getAuthenticationType() {
        String authenticationType = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new BaseException("Authentication has not token"));

        return AuthenticationType.valueOf(authenticationType);
    }

    public Long getTimeout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object credentials = authentication.getCredentials();
        if (credentials instanceof Claims) {
            return (((Claims) credentials).getExpiration().getTime() - new Date(System.currentTimeMillis()).getTime()) / 1000;
        }
        return -1L;
    }

    public TokenInformation getTokenInformation() {
        String userId = getUserId();
        Long timeout = getTimeout();

        return new TokenInformation()
                .setTimeout(timeout)
                .setUserId(userId);
    }
}
