package com.dianagrigore.rem.converter;


import com.dianagrigore.rem.dto.RegistrationDto;
import com.dianagrigore.rem.model.Registration;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConverter extends BasicMapper<Registration, RegistrationDto> {

    private final ObjectMapper objectMapper;

    public RegistrationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public RegistrationDto convertSource(Registration Registration, String expand) {
        return objectMapper.convertValue(Registration, RegistrationDto.class);
    }

    @Override
    public Registration convertTarget(RegistrationDto RegistrationDto) {
        return objectMapper.convertValue(RegistrationDto, Registration.class);
    }
}
