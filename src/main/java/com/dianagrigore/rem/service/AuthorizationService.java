package com.dianagrigore.rem.service;

import com.dianagrigore.rem.config.properties.SecurityProperties;
import com.dianagrigore.rem.converter.PasswordEncryptor;
import com.dianagrigore.rem.dto.LoginUserDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.dto.token.TokenDto;
import com.dianagrigore.rem.dto.token.TokenInformation;
import com.dianagrigore.rem.exception.LoginException;
import com.dianagrigore.rem.exception.PermissionDeniedException;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.model.UserLogin;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.repository.UserLoginRepository;
import com.dianagrigore.rem.repository.UserRepository;
import com.dianagrigore.rem.security.SecurityService;
import com.dianagrigore.rem.security.TokenService;
import com.dianagrigore.rem.web.converter.BasicMapper;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorizationService {

    private static final String GUEST = "GUEST";
    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    private final TokenService tokenService;
    private final SecurityService securityService;
    private final SecurityProperties securityProperties;
    private final BasicMapper<User, UserDto> userMapper;
    private final PasswordEncryptor passwordEncryptor;

    public AuthorizationService(UserRepository userRepository, UserLoginRepository userLoginRepository, TokenService tokenService, SecurityService securityService, SecurityProperties securityProperties,
            BasicMapper<User, UserDto> userMapper, PasswordEncryptor passwordEncryptor) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
        this.tokenService = tokenService;
        this.securityService = securityService;
        this.securityProperties = securityProperties;
        this.userMapper = userMapper;
        this.passwordEncryptor = passwordEncryptor;
    }

    public TokenDto login(LoginUserDto loginUser) {
        if (securityProperties.getAdminEnabled()) {
            String adminUserId = securityProperties.getAdminUserId();
            if (adminUserId.equals(loginUser.getLoginId()) && securityProperties.getAdminPassword().equals(loginUser.getPassword())) {
                return tokenService.generateToken(adminUserId, "ADMIN");
            }
            if (GUEST.equals(loginUser.getLoginId())) {
                return tokenService.generateToken(GUEST, GUEST);
            }
        }

        try {
            List<UserLogin> users = userLoginRepository.login(loginUser.getLoginId(), loginUser.getPassword());
            if (users.size() == 1) {
                User user = users.get(0).getUser();
                return tokenService.generateToken(user.getUserId(), user.getType().toString());
            } else if (users.size() > 1) {
                throw new LoginException("More than 1 user with this loginId and password! Contact administrator.");
            }
            throw new LoginException("Invalid password!");
        } catch (EntityNotFoundException exception) {
            throw new LoginException("User not fount!");
        }
    }

    public TokenDto refresh(String refreshToken) {

        Claims claims = tokenService.validateRefreshToken(refreshToken);
        String userId = claims.getSubject();
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isPresent()) {
            return tokenService.generateToken(userId, maybeUser.get().getType().toString());
        }
        throw new LoginException("Could not refresh token!");
    }

    public TokenInformation getTokenInformation() {
        return securityService.getTokenInformation();
    }

    public UserDto whoAmI() {
        String userIdFromContext = securityService.getUserId();

        if (securityProperties.getAdminEnabled() && securityProperties.getAdminUserId().equals(userIdFromContext)) {
            return UserDto.builder().userId(securityProperties.getAdminUserId()).type(UserType.ADMIN).name("Primary Admin").build();
        }
        if (GUEST.equals(userIdFromContext)) {
            return UserDto.builder().userId(securityProperties.getAdminUserId()).type(UserType.GUEST).name("Guest").build();
        }

        Optional<User> maybeUser = userRepository.findById(userIdFromContext);

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            return userMapper.convertSource(user);
        }
        throw new PermissionDeniedException("User doesn't exists");
    }
}
