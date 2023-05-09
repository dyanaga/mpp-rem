package com.dianagrigore.rem.converter;


import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BasicMapper<User, UserDto> {

    private final ObjectMapper objectMapper;

    public UserConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDto convertSource(User user, String expand) {
        return objectMapper.convertValue(user, UserDto.class);
    }

    @Override
    public User convertTarget(UserDto userDto) {
        return objectMapper.convertValue(userDto, User.class);
    }
}
