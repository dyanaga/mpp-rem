package com.dianagrigore.rem.service.review.impl;

import static java.util.Objects.nonNull;

import com.dianagrigore.rem.dto.ReviewDto;
import com.dianagrigore.rem.dto.pages.ReviewPage;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.Review;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.repository.ListingRepository;
import com.dianagrigore.rem.repository.ReviewRepository;
import com.dianagrigore.rem.repository.UserRepository;
import com.dianagrigore.rem.service.review.ReviewService;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.dianagrigore.rem.web.filter.FieldFilter;
import com.dianagrigore.rem.web.filter.FilterUtils;
import com.dianagrigore.rem.web.filter.specifications.IntegerSpecifications;
import com.dianagrigore.rem.web.filter.specifications.StringSpecifications;
import com.dianagrigore.rem.web.paging.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private static final List<String> FIND_ALLOWED_INTEGER_FILTERS = List.of("stars");
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ListingRepository agentRepository;
    private final BasicMapper<Review, ReviewDto> basicMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, ListingRepository agentRepository, BasicMapper<Review, ReviewDto> basicMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.basicMapper = basicMapper;
    }

    @Override
    public ReviewDto createReview(@NonNull String agentId, @Valid ReviewDto reviewToCreate) {
        logger.debug("Started to create review.");
        Review review = basicMapper.convertTarget(reviewToCreate);
        User user = getUserOrThrow(agentId);
        review.setUser(user);
        review.setUserId(user.getUserId());

        Review savedReview = reviewRepository.save(review);
        logger.debug("Review with id {} successfully created.", savedReview.getReviewId());

        return basicMapper.convertSource(savedReview, ExpandBuilder.of(ExpandableFields.USER).and(ExpandableFields.LISTING).toString());
    }

    @Override
    public ReviewPage findReviews(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort, @Nullable String expand) {
        return findReviews(null, filter, page, pageSize, sort, expand);
    }

    @Override
    public ReviewPage findReviewsForUser(@NonNull String userId, @Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand) {
        return findReviews(StringSpecifications.fieldEquals("userId", userId), filter, page, pageSize, sort, expand);
    }

    private ReviewPage findReviews(Specification<Review> indexSpecification, @Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand) {
        logger.debug("Find reviews called with parameters: filter=[{}], page=[{}], pageSize=[{}], sort=[{}], expand=[{}]", filter, page, pageSize, sort, expand);
        Pageable pageable = PageUtils.getPageable(page, pageSize, sort, "-timestamp");
        Map<String, FieldFilter> fieldFiltersMap = FilterUtils.getFieldFiltersMap(filter);

        List<Specification<Review>> specifications = new ArrayList<>();
        if (nonNull(indexSpecification)) {
            specifications.add(indexSpecification);

        }
        IntegerSpecifications.appendSpecifications(specifications, fieldFiltersMap, FIND_ALLOWED_INTEGER_FILTERS);

        Specification<Review> specification = specifications.stream().reduce(Specification::and).orElse(null);

        Page<Review> resultPage = reviewRepository.findAll(specification, pageable);
        logger.debug("Page of size=[{}] successfully got from the repository.", resultPage.getNumberOfElements());
        return new ReviewPage(resultPage, basicMapper.convertSource(resultPage.getContent(), expand));
    }

    @Override
    public ReviewDto getReview(@NonNull String reviewId) {
        logger.debug("Get review by id=[{}] requested.", reviewId);
        Review review = getReviewOrThrow(reviewId);
        logger.debug("Review with id=[{}] successfully found.", reviewId);
        return basicMapper.convertSource(review, ExpandBuilder.of(ExpandableFields.USER).and(ExpandableFields.LISTING).toString());
    }

    @Override
    public ReviewDto deleteReview(@NonNull String reviewId) {
        logger.debug("Delete review by id=[{}] requested.", reviewId);
        Review review = getReviewOrThrow(reviewId);

        reviewRepository.delete(review);

        logger.debug("Successfully deleted review id=[{}].", reviewId);
        return basicMapper.convertSource(review);
    }

    private Review getReviewOrThrow(@NonNull String reviewId) {
        Optional<Review> maybeReview = reviewRepository.findById(reviewId);
        if (maybeReview.isEmpty()) {
            throw new ResourceNotFoundException("Review with this reviewId does not exists!");
        }
        return maybeReview.get();
    }

    private User getUserOrThrow(@NonNull String userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new ResourceNotFoundException("User with given userId does not exists!");
        }
        return maybeUser.get();
    }

}
