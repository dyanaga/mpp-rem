package com.dianagrigore.rem.converter;

import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class OfferConverter extends BasicMapper<Offer, OfferDto> {

    private final ObjectMapper objectMapper;

    public OfferConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public OfferDto convertSource(Offer offer, String expand) {
        OfferDto offerDto = objectMapper.convertValue(offer, OfferDto.class);
        ExpandBuilder expandBuilder = ExpandBuilder.of(expand);
        if (expandBuilder.contains(ExpandableFields.USER)) {
            offerDto.setUser(objectMapper.convertValue(offer.getUser(), UserDto.class));
        }
        if (expandBuilder.contains(ExpandableFields.LISTING)) {
            offerDto.setListing(objectMapper.convertValue(offer.getListing(), ListingDto.class));
        }

        return offerDto;
    }

    @Override
    public Offer convertTarget(OfferDto offerDto) {
        return objectMapper.convertValue(offerDto, Offer.class);
    }
}
