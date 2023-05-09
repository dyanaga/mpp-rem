package com.dianagrigore.rem.service.review;

import com.dianagrigore.rem.dto.ReviewDto;
import com.dianagrigore.rem.dto.pages.ReviewPage;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.Valid;

/**
 * Service interface for review related operations
 */
public interface ReviewService {

    /**
     * Creates a new review.
     *
     * @param reviewToCreate - payload for creating the review
     * @return - new created review
     */
    ReviewDto createReview(@NonNull String agentId, @Valid ReviewDto reviewToCreate);

    /**
     * Search review using a filter and pagination parameters
     *
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged reviews by filter and having desired pagination.
     */
    ReviewPage findReviews(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand);

    /**
     * Search review for agent using a filter and pagination parameters
     *
     * @param userId   - Id of the user
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged reviews by filter and having desired pagination.
     */
    ReviewPage findReviewsForUser(@NonNull String userId, @Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand);

    /**
     * Searches for a specific review by id
     *
     * @param reviewId - the id of the review which is searched, cannot be null
     * @return - the desired review
     * @throws ResourceNotFoundException if the review was not found.
     */
    ReviewDto getReview(@NonNull String reviewId);

    /**
     * Delete an review based on the id.
     *
     * @param reviewId - the id of the review which is searched, cannot be null
     * @return - the updated review
     * @throws ResourceNotFoundException if the review was not found.
     */
    ReviewDto deleteReview(@NonNull String reviewId);

}
