package com.dianagrigore.rem.api;

import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.pages.OfferPage;
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
@Api(value = "Offers")
public interface OfferApi {
    String CREATE_OFFER = "/listings/{listing-id}/offers";
    String FIND_OFFERS = "/offers";
    String USERS_OFFERS = "/users/{user-id}/offers";
    String GET_OFFER = "/offers/{offer-id}";
    String UPDATE_OFFER = "/offers/{offer-id}";
    String DELETE_OFFER = "/offers/{offer-id}";
    String UPDATE_OFFER_USER = "/users/{user-id}/offers/{offer-id}";
    String DELETE_OFFER_USER = "/users/{user-id}/offers/{offer-id}";

    /**
     * Creates a new offer.
     *
     * @param listingId - id of the listing
     * @param offer     - payload for creating the offer
     * @return - new created offer
     * @apiNote - POST /listings/{listing-id}/offers
     */
    @ApiOperation(value = "Creates a new offer.", nickname = "createOffer", notes = "Creates a new offer.", response = OfferDto.class, tags = {"offers",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Newly created offer with offerId.", response = OfferDto.class), @ApiResponse(code = 400, message = "unexpected error"
            , response = ResponseException.class)})
    @RequestMapping(value = CREATE_OFFER, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)

    default OfferDto createOffer(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId,
            @ApiParam(value = "The body of the offer.") @Valid @RequestBody OfferDto offer) {
        return new OfferDto();
    }

    @RequestMapping(value = {UPDATE_OFFER_USER, UPDATE_OFFER}, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.PUT)

    default OfferDto updateOffer(@PathVariable(value = "user-id", required = false) String userId, @PathVariable("offer-id") String offerId, @Valid @RequestBody OfferDto offer) {
        return new OfferDto();
    }

    /**
     * Search offer for listing using a filter and pagination parameters
     *
     * @param filter    - basic FIQL filter, might be null
     * @param page      - desired page, might be null and default will be 0.
     * @param pageSize  - desired page size, might be null and default will be 100.
     * @param sort      - direction of the sort, might be null, but default will be name ascending.
     * @param expand    - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged offers by filter and having desired pagination.
     * @apiNote - GET /listings/{listing-id}/offers
     */
    @ApiOperation(value = "Returns paged response of Offers for listing", nickname = "findOffersForListing", notes = "Endpoint used to return all offers items.", response =
            OfferPage.class, tags = {
            "offers",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Offers paged items.", response = OfferPage.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = FIND_OFFERS, produces = {"application/json"}, method = RequestMethod.GET)
    default OfferPage findOffers(@ApiParam(value = "Basic FIQL filter") @Valid @RequestParam(required = false) String filter,

            @Min(0) @ApiParam(value = "The index of the desired page.", defaultValue = "0") @Valid @RequestParam(required = false, defaultValue = "0") Integer page,

            @Min(1) @Max(200) @ApiParam(value = "The size of the page, total number of items displayed for a page", defaultValue = "100") @Valid @RequestParam(required = false,
                    defaultValue = "100") Integer pageSize,

            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @ApiParam(value = "Attributes to sort by, something like +attribute.name") @Valid @RequestParam(required = false) String sort,

            @Pattern(regexp = "\\w+(,\\w+)*") @ApiParam(value = "Attributes to expand, something like 'account' ") @Valid @RequestParam(required = false) String expand) {

        return OfferPage.EMPTY;
    }

    /**
     * Search offer for user using a filter and pagination parameters
     *
     * @param userId   - id of the listing
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged offers by filter and having desired pagination.
     * @apiNote - GET /users/{user-id}/offers
     */
    @ApiOperation(value = "Returns paged response of Offers for listing", nickname = "findOffersForUser", notes = "Endpoint used to return all offers items.", response =
            OfferPage.class, tags = {
            "offers",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Offers paged items.", response = OfferPage.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = USERS_OFFERS, produces = {"application/json"}, method = RequestMethod.GET)
    default OfferPage findOffersForUser(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("user-id") String userId,
            @ApiParam(value = "Basic FIQL filter") @Valid @RequestParam(required = false) String filter,

            @Min(0) @ApiParam(value = "The index of the desired page.", defaultValue = "0") @Valid @RequestParam(required = false, defaultValue = "0") Integer page,

            @Min(1) @Max(200) @ApiParam(value = "The size of the page, total number of items displayed for a page", defaultValue = "100") @Valid @RequestParam(required = false,
                    defaultValue = "100") Integer pageSize,

            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @ApiParam(value = "Attributes to sort by, something like +attribute.name") @Valid @RequestParam(required = false) String sort,

            @Pattern(regexp = "\\w+(,\\w+)*") @ApiParam(value = "Attributes to expand, something like 'account' ") @Valid @RequestParam(required = false) String expand) {

        return OfferPage.EMPTY;
    }

    /**
     * Searches for a specific offer by id
     *
     * @param offerId - the id of the offer which is searched, cannot be null
     * @return - the desired offer
     * @apiNote - GET /offers/{offer-id}
     */
    @ApiOperation(value = "Returns singular offer", nickname = "getOffer", notes = "Endpoint used to return a offer item.", response = OfferDto.class, tags = {"offers",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Offer item.", response = OfferDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = GET_OFFER, produces = {"application/json"}, method = RequestMethod.GET)
    default OfferDto getOffer(@ApiParam(value = "The id of the offer.", required = true) @PathVariable("offer-id") String offerId) {
        return new OfferDto();
    }

    /**
     * Deletes an offer based on the id. It will also deactivate each account with each card of the offer.
     *
     * @param offerId - the id of the offer which is searched, cannot be null
     * @return - the updated offer
     * @apiNote - DELETE /offers/{offer-id}
     */
    @ApiOperation(value = "Deletes singular offer", nickname = "deactivateOffer", notes = "Endpoint used to deactivate a offer.", response = OfferDto.class, tags = {"offers",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Offer deleted successfully.", response = OfferDto.class), @ApiResponse(code = 400, message = "unexpected error",
            response = ResponseException.class)})
    @RequestMapping(value = {DELETE_OFFER, DELETE_OFFER_USER}, produces = {"application/json"}, method = RequestMethod.DELETE)

    default OfferDto deleteOffer(@ApiParam(value = "The id of the offer.", required = true) @PathVariable("offer-id") String offerId, @PathVariable(value = "user-id", required = false) String userId) {
        return new OfferDto();
    }
}
