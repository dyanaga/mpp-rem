package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.dto.LoginUserDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.dto.token.TokenDto;
import com.dianagrigore.rem.dto.token.TokenInformation;
import com.dianagrigore.rem.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")

public class InitializationController {

    private final AuthorizationService authorizationService;

    public InitializationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginUserDto loginUserDto) {
        TokenDto login = authorizationService.login(loginUserDto);
        return ResponseEntity.ok(login);
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


