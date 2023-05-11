package com.dianagrigore.rem.converter;


import com.dianagrigore.rem.dto.RegistrationDto;
import com.dianagrigore.rem.model.UserLogin;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConverter extends BasicMapper<UserLogin, RegistrationDto> {

    private final ObjectMapper objectMapper;

    public RegistrationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RegistrationDto convertSource(UserLogin UserLogin, String expand) {
        return objectMapper.convertValue(UserLogin, RegistrationDto.class);
    }

    @Override
    public UserLogin convertTarget(RegistrationDto RegistrationDto) {
        return objectMapper.convertValue(RegistrationDto, UserLogin.class);
    }
}
