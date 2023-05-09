package com.dianagrigore.rem.api;

import com.dianagrigore.rem.dto.ReviewDto;
import com.dianagrigore.rem.dto.pages.ReviewPage;
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
@Api(value = "Reviews")
public interface ReviewApi {
    String CREATE_REVIEW = "/users/{listing-id}/reviews";
    String FIND_REVIEWS = "/reviews";
    String USERS_REVIEWS = "/users/{user-id}/reviews";
    String GET_REVIEW = "/reviews/{review-id}";
    String DELETE_REVIEW = "/reviews/{review-id}";

    /**
     * Creates a new review.
     *
     * @param listingId - id of the listing
     * @param review     - payload for creating the review
     * @return - new created review
     * @apiNote - POST /users/{listing-id}/reviews
     */
    @ApiOperation(value = "Creates a new review.", nickname = "createReview", notes = "Creates a new review.", response = ReviewDto.class, tags = {"reviews",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Newly created review with reviewId.", response = ReviewDto.class), @ApiResponse(code = 400, message = "unexpected error"
            , response = ResponseException.class)})
    @RequestMapping(value = CREATE_REVIEW, produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)

    default ReviewDto createReview(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("listing-id") String listingId,
            @ApiParam(value = "The body of the review.") @Valid @RequestBody ReviewDto review) {
        return new ReviewDto();
    }

    /**
     * Search review for listing using a filter and pagination parameters
     *
     * @param filter    - basic FIQL filter, might be null
     * @param page      - desired page, might be null and default will be 0.
     * @param pageSize  - desired page size, might be null and default will be 100.
     * @param sort      - direction of the sort, might be null, but default will be name ascending.
     * @param expand    - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged reviews by filter and having desired pagination.
     * @apiNote - GET /users/{listing-id}/reviews
     */
    @ApiOperation(value = "Returns paged response of Reviews", nickname = "findReviews", notes = "Endpoint used to return all reviews items.", response =
            ReviewPage.class, tags = {
            "reviews",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Reviews paged items.", response = ReviewPage.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = FIND_REVIEWS, produces = {"application/json"}, method = RequestMethod.GET)
    default ReviewPage findReviews(@ApiParam(value = "Basic FIQL filter") @Valid @RequestParam(required = false) String filter,

            @Min(0) @ApiParam(value = "The index of the desired page.", defaultValue = "0") @Valid @RequestParam(required = false, defaultValue = "0") Integer page,

            @Min(1) @Max(200) @ApiParam(value = "The size of the page, total number of items displayed for a page", defaultValue = "100") @Valid @RequestParam(required = false,
                    defaultValue = "100") Integer pageSize,

            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @ApiParam(value = "Attributes to sort by, something like +attribute.name") @Valid @RequestParam(required = false) String sort,

            @Pattern(regexp = "\\w+(,\\w+)*") @ApiParam(value = "Attributes to expand, something like 'account' ") @Valid @RequestParam(required = false) String expand) {

        return ReviewPage.EMPTY;
    }

    /**
     * Search review for user using a filter and pagination parameters
     *
     * @param userId   - id of the listing
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged reviews by filter and having desired pagination.
     * @apiNote - GET /users/{user-id}/reviews
     */
    @ApiOperation(value = "Returns paged response of Reviews for listing", nickname = "findReviewsForUser", notes = "Endpoint used to return all reviews items.", response =
            ReviewPage.class, tags = {
            "reviews",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Reviews paged items.", response = ReviewPage.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = USERS_REVIEWS, produces = {"application/json"}, method = RequestMethod.GET)
    default ReviewPage findReviewsForUser(@ApiParam(value = "The id of the listing.", required = true) @PathVariable("user-id") String userId,
            @ApiParam(value = "Basic FIQL filter") @Valid @RequestParam(required = false) String filter,

            @Min(0) @ApiParam(value = "The index of the desired page.", defaultValue = "0") @Valid @RequestParam(required = false, defaultValue = "0") Integer page,

            @Min(1) @Max(200) @ApiParam(value = "The size of the page, total number of items displayed for a page", defaultValue = "100") @Valid @RequestParam(required = false,
                    defaultValue = "100") Integer pageSize,

            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @ApiParam(value = "Attributes to sort by, something like +attribute.name") @Valid @RequestParam(required = false) String sort,

            @Pattern(regexp = "\\w+(,\\w+)*") @ApiParam(value = "Attributes to expand, something like 'account' ") @Valid @RequestParam(required = false) String expand) {

        return ReviewPage.EMPTY;
    }

    /**
     * Searches for a specific review by id
     *
     * @param reviewId - the id of the review which is searched, cannot be null
     * @return - the desired review
     * @apiNote - GET /reviews/{review-id}
     */
    @ApiOperation(value = "Returns singular review", nickname = "getReview", notes = "Endpoint used to return a review item.", response = ReviewDto.class, tags = {"reviews",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Review item.", response = ReviewDto.class), @ApiResponse(code = 400, message = "unexpected error", response =
            ResponseException.class)})
    @RequestMapping(value = GET_REVIEW, produces = {"application/json"}, method = RequestMethod.GET)
    default ReviewDto getReview(@ApiParam(value = "The id of the review.", required = true) @PathVariable("review-id") String reviewId) {
        return new ReviewDto();
    }

    /**
     * Deletes an review based on the id. It will also deactivate each account with each card of the review.
     *
     * @param reviewId - the id of the review which is searched, cannot be null
     * @return - the updated review
     * @apiNote - DELETE /reviews/{review-id}
     */
    @ApiOperation(value = "Deletes singular review", nickname = "deactivateReview", notes = "Endpoint used to deactivate a review.", response = ReviewDto.class, tags = {"reviews",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Review deleted successfully.", response = ReviewDto.class), @ApiResponse(code = 400, message = "unexpected error",
            response = ResponseException.class)})
    @RequestMapping(value = DELETE_REVIEW, produces = {"application/json"}, method = RequestMethod.DELETE)

    default ReviewDto deleteReview(@ApiParam(value = "The id of the review.", required = true) @PathVariable("review-id") String reviewId) {
        return new ReviewDto();
    }
}
