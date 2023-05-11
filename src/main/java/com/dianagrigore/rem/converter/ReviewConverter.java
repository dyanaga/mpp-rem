package com.dianagrigore.rem.converter;

import com.dianagrigore.rem.dto.ReviewDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.Review;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter extends BasicMapper<Review, ReviewDto> {

    private final ObjectMapper objectMapper;

    public ReviewConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ReviewDto convertSource(Review review, String expand) {
        ReviewDto reviewDto = objectMapper.convertValue(review, ReviewDto.class);
        ExpandBuilder expandBuilder = ExpandBuilder.of(expand);
        if (expandBuilder.contains(ExpandableFields.USER)) {
            reviewDto.setUser(objectMapper.convertValue(review.getUser(), UserDto.class));
        }
        if (expandBuilder.contains(ExpandableFields.CREATOR)) {
            reviewDto.setCreatorUser(objectMapper.convertValue(review.getCreatorUser(), UserDto.class));
        }
        return reviewDto;
    }

    @Override
    public Review convertTarget(ReviewDto reviewDto) {
        return objectMapper.convertValue(reviewDto, Review.class);
    }
}
