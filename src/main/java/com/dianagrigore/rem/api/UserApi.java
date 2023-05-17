package com.dianagrigore.rem.api;

import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.dto.pages.UserPage;
import com.dianagrigore.rem.exception.ResponseException;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.permissions.PermissionCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Validated
@Api(value = "Users")
public interface UserApi {
    String CREATE_USER = "/users";
    String FIND_USERS = "/users";
    String GET_USER = "/users/{user-id}";
    String GET_USER_PROFILE = "/users/profile";
    String UPDATE_USER = "/users/{user-id}";
    String DEACTIVATE_USER = "/users/{user-id}";
    String PAGE_SIZE = "/users-global/page-size/{page-size}";

    /**
     * Creates a new user.
     *
     * @param user - payload for creating the user
     * @return - new created user
     * @apiNote - POST /users
     */
    @ApiOperation(value = "Creates a new user.", nickname = "createUser", notes = "Creates a new user.", response = UserDto.class, tags = {"users",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Newly created user with userId.", response = UserDto.class), @ApiResponse(code = 400, message = "unexpected error"
            , response = ResponseException.class)})
    @RequestMapping(value = CREATE_USER, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)

    default UserDto createUser(@ApiParam(value = "The body of the user.") @Valid @RequestBody UserDto user) {
        return new UserDto();
    }

    /**
     * Search user using a filter and pagination parameters
     *
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged users by filter and having desired pagination.
     * @apiNote - GET /users
     */
    @ApiOperation(value = "Returns paged response of Users", nickname = "findUsers", notes = "Endpoint used to return all users items.", response = UserPage.class, tags = {
            "users",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Users paged items.", response = UserPage.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = FIND_USERS, produces = {"application/json"}, method = RequestMethod.GET)
    default UserPage findUsers(@ApiParam(value = "Basic FIQL filter") @Valid @RequestParam(required = false) String filter,

            @Min(0) @ApiParam(value = "The index of the desired page.", defaultValue = "0") @Valid @RequestParam(required = false, defaultValue = "0") Integer page,

            @Min(1) @Max(200) @ApiParam(value = "The size of the page, total number of items displayed for a page", defaultValue = "100") @Valid @RequestParam(required = false,
                    defaultValue = "100") Integer pageSize,

            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @ApiParam(value = "Attributes to sort by, something like +attribute.name") @Valid @RequestParam(required = false) String sort,

            @Pattern(regexp = "\\w+(,\\w+)*") @ApiParam(value = "Attributes to expand, something like 'account' ") @Valid @RequestParam(required = false) String expand) {

        return UserPage.EMPTY;
    }

    /**
     * Searches for a specific user by id
     *
     * @param userId - the id of the user which is searched, cannot be null
     * @return - the desired user
     * @apiNote - GET /users/{user-id}
     */
    @ApiOperation(value = "Returns singular user", nickname = "getUser", notes = "Endpoint used to return a user item.", response = UserDto.class, tags = {"users",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User item.", response = UserDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = GET_USER, produces = {"application/json"}, method = RequestMethod.GET)
    default UserDto getUser(@ApiParam(value = "The id of the user.", required = true) @PathVariable("user-id") String userId) {
        return new UserDto();
    }

    /**
     * Returns the information about the current user.
     *
     * @return - the desired user
     * @apiNote - GET /users/{user-id}
     */
    @ApiOperation(value = "Returns the current users profile", nickname = "getUserProfile", notes = "Endpoint used to return the current user.", response = UserDto.class, tags =
            {"users",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User item.", response = UserDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = GET_USER_PROFILE, produces = {"application/json"}, method = RequestMethod.GET)
    default UserDto getUserProfile() {
        return new UserDto();
    }

    /**
     * Updates a user with the given updates (password field is ignored). It is also recursive, so the changes given for the accounts
     * or cards will be modified as well
     *
     * @param userId          - the id of the user which is wanted to be updated, cannot be null.
     * @param userWithUpdates - the user with the updated fields.
     * @return - the updated user.
     * @apiNote - PUT /users/{user-id}
     */
    @ApiOperation(value = "Updates an existing user.", nickname = "updateUser", notes = "Updates an existing user.", response = UserDto.class, tags = {"users",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Updated user.", response = UserDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = UPDATE_USER, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.PUT)

    default UserDto updateUser(@ApiParam(value = "The id of the user.", required = true) @PathVariable("user-id") String userId,
            @ApiParam(value = "The body of the user.") @Valid @RequestBody UserDto userWithUpdates) {
        return new UserDto();
    }

    /**
     * Deletes a user based on the id. It will also deactivate each account with each card of the user.
     *
     * @param userId - the id of the user which is searched, cannot be null
     * @return - the updated user
     * @apiNote - DELETE /users/{user-id}
     */
    @ApiOperation(value = "Deletes singular user", nickname = "deactivateUser", notes = "Endpoint used to deactivate a user.", response = UserDto.class, tags = {"users",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User deleted successfully.", response = UserDto.class), @ApiResponse(code = 400, message = "unexpected error",
            response = ResponseException.class)})
    @RequestMapping(value = DEACTIVATE_USER, produces = {"application/json"}, method = RequestMethod.DELETE)

    default UserDto deleteUser(@ApiParam(value = "The id of the user.", required = true) @PathVariable("user-id") String userId) {
        return new UserDto();
    }

    @RequestMapping(value = PAGE_SIZE, method = RequestMethod.POST)
    void setPagePreference(@Min(1) @Max(100) @PathVariable("page-size") int pagePreference);
}
