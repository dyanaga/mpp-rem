package com.dianagrigore.rem.converter;

import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListingConverter extends BasicMapper<Listing, ListingDto> {

    private final ObjectMapper objectMapper;

    public ListingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ListingDto convertSource(Listing listing, String expand) {
        ListingDto listingDto = objectMapper.convertValue(listing, ListingDto.class);
        ExpandBuilder expandBuilder = ExpandBuilder.of(expand);
        if (expandBuilder.contains(ExpandableFields.USERS)) {
            List<UserDto> users = listing.getUsers().stream().map(agentListing -> objectMapper.convertValue(agentListing.getUser(), UserDto.class)).toList();
            listingDto.setUsers(users);
        }
        if (expandBuilder.contains(ExpandableFields.CREATOR)) {
            listingDto.setCreatorUser(objectMapper.convertValue(listing.getCreatorUser(), UserDto.class));
        }
        if (expandBuilder.contains(ExpandableFields.OFFERS)) {
            List<OfferDto> offers = listing.getOffers().stream().map(offer -> {
                OfferDto offerDto = objectMapper.convertValue(offer, OfferDto.class);
                offerDto.setUser(objectMapper.convertValue(offer.getUser(), UserDto.class));
                return offerDto;
            }).toList();
            listingDto.setOffers(offers);
        }

        return listingDto;
    }

    @Override
    public Listing convertTarget(ListingDto listingDto) {
        return objectMapper.convertValue(listingDto, Listing.class);
    }
}
