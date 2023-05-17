package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.api.UserApi;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.dto.pages.UserPage;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.permissions.PermissionCheck;
import com.dianagrigore.rem.service.user.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/v1")
public class UserController implements UserApi {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    @PermissionCheck(hasAny = {UserType.ADMIN})
    public UserDto createUser(@Valid UserDto user) {
        return userService.createUser(user);
    }

    @Override
    public UserPage findUsers(@Valid String filter, @Min(0) @Valid Integer page, @Min(1) @Max(200) @Valid Integer pageSize,
            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @Valid String sort, @Pattern(regexp = "\\w+(,\\w+)*") @Valid String expand) {
        return userService.findUsers(filter, page, pageSize, sort, expand);
    }

    @Override
    public UserDto getUser(String userId) {
        return userService.getUser(userId);
    }

    @Override
    public UserDto getUserProfile() {
        return userService.getUserProfile();
    }

    @Override
    public UserDto updateUser(String userId, @Valid UserDto userWithUpdates) {
        return userService.updateUser(userId, userWithUpdates);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.ADMIN})
    public UserDto deleteUser(String userId) {
        return userService.deleteUser(userId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.ADMIN})
    public void setPagePreference(int pagePreference) {
        userService.setPagePreferenceForAllUsers(pagePreference);
    }

}
