package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.api.ReviewApi;
import com.dianagrigore.rem.dto.ReviewDto;
import com.dianagrigore.rem.dto.pages.ReviewPage;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.permissions.PermissionCheck;
import com.dianagrigore.rem.service.review.ReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ReviewController implements ReviewApi {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public ReviewDto createReview(String listingId, ReviewDto review) {
        return reviewService.createReview(listingId, review);
    }

    @Override
    public ReviewPage findReviews(String filter, Integer page, Integer pageSize, String sort, String expand) {
        return reviewService.findReviews(filter, page, pageSize, sort, expand);
    }

    @Override
    public ReviewPage findReviewsForUser(String userId, String filter, Integer page, Integer pageSize, String sort, String expand) {
        return reviewService.findReviewsForUser(userId, filter, page, pageSize, sort, expand);
    }

    @Override
    public ReviewDto getReview(String reviewId) {
        return reviewService.getReview(reviewId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public ReviewDto deleteReview(String reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}
