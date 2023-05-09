package com.dianagrigore.rem.service.user;

import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.dto.pages.UserPage;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.Valid;

/**
 * Service interface for user related operations
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userToCreate - payload for creating the user
     * @return - new created user
     */
    UserDto createUser(@Valid UserDto userToCreate);

    /**
     * Search user using a filter and pagination parameters
     *
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged users by filter and having desired pagination.
     */
    UserPage findUsers(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort, @Nullable String expand);

    /**
     * Searches for a specific user by id
     *
     * @param userId - the id of the user which is searched, cannot be null
     * @return - the desired user
     * @throws ResourceNotFoundException if the user was not found.
     */
    UserDto getUser(@NonNull String userId);

    /**
     * Returns the information about the current user.
     *
     * @return - the current user information
     * @throws ResourceNotFoundException if the user was not found.
     */
    UserDto getUserProfile();

    /**
     * Updates a user with the given updates (password field is ignored).
     * or cards will be modified as well
     *
     * @param userId          - the id of the user which is wanted to be updated, cannot be null.
     * @param userWithUpdates - the user with the updated fields.
     * @return - the updated user.
     * @throws ResourceNotFoundException if the user was not found.
     */
    UserDto updateUser(@NonNull String userId, @Valid UserDto userWithUpdates);

    /**
     * Deactivates(soft delete) a user based on the id.
     *
     * @param userId - the id of the user which is searched, cannot be null
     * @return - the updated user
     * @throws ResourceNotFoundException if the user was not found.
     */
    UserDto deleteUser(@NonNull String userId);


}
