package com.dianagrigore.rem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationDto {

    private String userId;
    private Date timestamp;
}