package com.dianagrigore.rem.api;

import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.pages.ListingPage;
import com.dianagrigore.rem.exception.ResponseException;
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
@Api(value = "Listings")
public interface ListingApi {
    String CREATE_LISTING = "/listings";
    String FIND_LISTINGS = "/listings";
    String GET_LISTING = "/listings/{listing-id}";
    String UPDATE_LISTING = "/listings/{listing-id}";
    String DEACTIVATE_LISTING = "/listings/{listing-id}";
    String ENROLL_LISTING = "/listings/{listing-id}/enroll";
    String ENROLL_LISTING_OTHER = "/listings/{listing-id}/enroll/{user-id}";

    /**
     * Creates a new listing.
     *
     * @param listing - payload for creating the listing
     * @return - new created listing
     * @apiNote - POST /listings
     */
    @ApiOperation(value = "Creates a new listing.", nickname = "createListing", notes = "Creates a new listing.", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Newly created listing with listingId.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error"
            , response = ResponseException.class)})
    @RequestMapping(value = CREATE_LISTING, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)

    default ListingDto createListing(@ApiParam(value = "The body of the listing.") @Valid @RequestBody ListingDto listing) {
        return new ListingDto();
    }

    /**
     * Search listing using a filter and pagination parameters
     *
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged listings by filter and having desired pagination.
     * @apiNote - GET /listings
     */
    @ApiOperation(value = "Returns paged response of Listings", nickname = "findListings", notes = "Endpoint used to return all listings items.", response = ListingPage.class, tags = {
            "listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Listings paged items.", response = ListingPage.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = FIND_LISTINGS, produces = {"application/json"}, method = RequestMethod.GET)
    default ListingPage findListings(@ApiParam(value = "Basic FIQL filter") @Valid @RequestParam(required = false) String filter,

            @Min(0) @ApiParam(value = "The index of the desired page.", defaultValue = "0") @Valid @RequestParam(required = false, defaultValue = "0") Integer page,

            @Min(1) @Max(200) @ApiParam(value = "The size of the page, total number of items displayed for a page", defaultValue = "100") @Valid @RequestParam(required = false,
                    defaultValue = "100") Integer pageSize,

            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @ApiParam(value = "Attributes to sort by, something like +attribute.name") @Valid @RequestParam(required = false) String sort,

            @Pattern(regexp = "\\w+(,\\w+)*") @ApiParam(value = "Attributes to expand, something like 'account' ") @Valid @RequestParam(required = false) String expand) {

        return ListingPage.EMPTY;
    }

    /**
     * Searches for a specific listing by id
     *
     * @param listingId - the id of the listing which is searched, cannot be null
     * @return - the desired listing
     * @apiNote - GET /listings/{listing-id}
     */
    @ApiOperation(value = "Returns singular listing", nickname = "getListing", notes = "Endpoint used to return a listing item.", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Listing item.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = GET_LISTING, produces = {"application/json"}, method = RequestMethod.GET)
    default ListingDto getListing(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId) {
        return new ListingDto();
    }

    /**
     * Updates a listing with the given updates (password field is ignored). It is also recursive, so the changes given for the accounts
     * or cards will be modified as well
     *
     * @param listingId          - the id of the listing which is wanted to be updated, cannot be null.
     * @param listingWithUpdates - the listing with the updated fields.
     * @return - the updated listing.
     * @apiNote - PUT /listings/{listing-id}
     */
    @ApiOperation(value = "Updates an existing listing.", nickname = "updateListing", notes = "Updates an existing listing.", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Updated listing.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = UPDATE_LISTING, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.PUT)

    default ListingDto updateListing(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId,
            @ApiParam(value = "The body of the listing.") @Valid @RequestBody ListingDto listingWithUpdates) {
        return new ListingDto();
    }

    /**
     * Deletes a listing based on the id. It will also deactivate each account with each card of the listing.
     *
     * @param listingId - the id of the listing which is searched, cannot be null
     * @return - the updated listing
     * @apiNote - DELETE /listings/{listing-id}
     */
    @ApiOperation(value = "Deletes singular listing", nickname = "deactivateListing", notes = "Endpoint used to deactivate a listing.", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Listing deleted successfully.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error",
            response = ResponseException.class)})
    @RequestMapping(value = DEACTIVATE_LISTING, produces = {"application/json"}, method = RequestMethod.DELETE)

    default ListingDto deleteListing(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId) {
        return new ListingDto();
    }

    /**
     * Adds the current agent to the listing
     *
     * @param listingId          - the id of the listing which is wanted to be updated, cannot be null.
     * @return - the updated listing.
     * @apiNote - POST /listings/{listing-id}/enroll
     */
    @ApiOperation(value = "Enrolls current user to listing", nickname = "enrollListing", notes = "Enrolls current user to listing", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Current user enrolled.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = ENROLL_LISTING, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)

    default ListingDto enrollListing(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId) {
        return new ListingDto();
    }

    /**
     * Adds the specified agent to the listing
     *
     * @param listingId          - the id of the listing which is wanted to be updated, cannot be null.
     * @param userId          - the id of the user which is wanted to be enrolled, cannot be null, user must be at least an AGENT.
     * @return - the updated listing.
     * @apiNote - POST /listings/{listing-id}/enroll/{user-id}
     */
    @ApiOperation(value = "Enrolls specified user to listing", nickname = "enrollListingSpecified", notes = "Enrolls specified user to listing", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Agent enlisted.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = ENROLL_LISTING_OTHER, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)

    default ListingDto enrollListingSpecific(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId,
            @ApiParam(value = "The id of the user.", required = true) @PathVariable("user-id") String userId) {
        return new ListingDto();
    }

    /**
     * Removes the current agent to the listing
     *
     * @param listingId          - the id of the listing which is wanted to be updated, cannot be null.
     * @return - the updated listing.
     * @apiNote - POST /listings/{listing-id}/enroll
     */
    @ApiOperation(value = "Quits current user from the listing", nickname = "quitListing", notes = "Quits current user from the listing", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Current user quit.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = ENROLL_LISTING, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.DELETE)

    default ListingDto quitListing(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId) {
        return new ListingDto();
    }

    /**
     * Removes the specified agent to the listing
     *
     * @param listingId          - the id of the listing which is wanted to be updated, cannot be null.
     * @param userId          - the id of the user which is wanted to be removed from the listing, cannot be null, user must be at least an AGENT.
     * @return - the updated listing.
     * @apiNote - POST /listings/{listing-id}/enroll/{user-id}
     */
    @ApiOperation(value = "Quits specified user from the listing", nickname = "enrollListingSpecified", notes = "Quits specified user from the listing", response = ListingDto.class, tags = {"listings",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Agent quit.", response = ListingDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = ENROLL_LISTING_OTHER, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.DELETE)

    default ListingDto quitListingSpecific(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId,
            @ApiParam(value = "The id of the user.", required = true) @PathVariable("user-id") String userId) {
        return new ListingDto();
    }
}
