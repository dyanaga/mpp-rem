package com.dianagrigore.rem.converter;


import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.ReviewDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.model.Review;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter extends BasicMapper<User, UserDto> {

    private final BasicMapper<Offer, OfferDto> offerConverter;
    private final BasicMapper<Review, ReviewDto> reviewConverter;
    private final BasicMapper<Listing, ListingDto> listingConverter;
    private final ObjectMapper objectMapper;

    public UserConverter(BasicMapper<Offer, OfferDto> offerConverter, BasicMapper<Review, ReviewDto> reviewConverter, BasicMapper<Listing, ListingDto> listingConverter, ObjectMapper objectMapper) {
        this.offerConverter = offerConverter;
        this.reviewConverter = reviewConverter;
        this.listingConverter = listingConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDto convertSource(User user, String expand) {
        UserDto userDto = objectMapper.convertValue(user, UserDto.class);
        ExpandBuilder expandBuilder = ExpandBuilder.of(expand);
        userDto.setUsername(user.getLogin().getUsername());
        if(expandBuilder.contains(ExpandableFields.OFFERS)) {
            userDto.setOffers(offerConverter.convertSource(user.getOffers()));
        }
        if(expandBuilder.contains(ExpandableFields.REVIEWS)) {
            userDto.setReviews(reviewConverter.convertSource(user.getReviews()));
        }
        if(expandBuilder.contains(ExpandableFields.WRITTEN_REVIEWS)) {
            userDto.setWrittenReviews(reviewConverter.convertSource(user.getReviewsCreated()));
        }
        if(expandBuilder.contains(ExpandableFields.LISTINGS_CREATED)) {
            List<ListingDto> listings = user.getListingsCreated().stream().map(agentListing -> listingConverter.convertSource(agentListing.getListing())).toList();
            userDto.setListingsCreated(listings);
        }
        if(expandBuilder.contains(ExpandableFields.LISTINGS)) {
            List<ListingDto> listings = user.getListings().stream().map(agentListing -> listingConverter.convertSource(agentListing.getListing())).toList();
            userDto.setListings(listings);
        }
        return userDto;
    }

    @Override
    public User convertTarget(UserDto userDto) {
        return objectMapper.convertValue(userDto, User.class);
    }
}
