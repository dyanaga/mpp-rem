package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.dto.LoginUserDto;
import com.dianagrigore.rem.dto.RegistrationDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.dto.token.TokenDto;
import com.dianagrigore.rem.dto.token.TokenInformation;
import com.dianagrigore.rem.service.AuthorizationService;
import com.dianagrigore.rem.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
public class InitializationController {

    private final AuthorizationService authorizationService;
    private final UserService userService;

    public InitializationController(AuthorizationService authorizationService, UserService userService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginUserDto loginUserDto) {
        TokenDto login = authorizationService.login(loginUserDto);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationDto> register(@RequestBody @Valid UserDto registerUser) {
        RegistrationDto registerDto = userService.registerUser(registerUser);
        return ResponseEntity.ok(registerDto);
    }

    @PostMapping("/register/confirm/{user-id}")
    public ResponseEntity<UserDto> activate(@PathVariable("user-id") String parameter) {
        UserDto userDto = userService.activateUser(parameter);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/token-information")
    public ResponseEntity<TokenInformation> getTokenInformation() {
        return ResponseEntity.ok(authorizationService.getTokenInformation());
    }

    @GetMapping("/who-am-i")
    public ResponseEntity<UserDto> whoAmI() {
        return ResponseEntity.ok(authorizationService.whoAmI());
    }

}


