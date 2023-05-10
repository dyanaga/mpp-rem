package com.dianagrigore.rem.converter;


import static java.util.Objects.nonNull;

import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BasicMapper<User, UserDto> {

    private final BasicMapper<Offer, OfferDto> offerConverter;
    private final ObjectMapper objectMapper;

    public UserConverter(BasicMapper<Offer, OfferDto> offerConverter, ObjectMapper objectMapper) {
        this.offerConverter = offerConverter;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDto convertSource(User user, String expand) {
        UserDto userDto = objectMapper.convertValue(user, UserDto.class);
        ExpandBuilder expandBuilder = ExpandBuilder.of(expand);
        if(expandBuilder.contains(ExpandableFields.OFFERS)) {
            userDto.setOffers(offerConverter.convertSource(user.getOffers()));
        }
        return userDto;
    }

    @Override
    public User convertTarget(UserDto userDto) {
        return objectMapper.convertValue(userDto, User.class);
    }
}
