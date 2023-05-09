package com.dianagrigore.rem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingDto {

    private String listingId;

    @NotBlank(message = "Name should not be empty!")
    @Size(message = "The name should be shorter than 100 characters but longer than 3", max = 100, min = 3)
    private String name;

    @NotBlank(message = "Address should not be empty!")
    @Size(message = "The address should be shorter than 100 characters but longer than 3", max = 200, min = 3)
    private String address;

    @Min(value = 1, message = "At least 1 room is needed")
    private int rooms = 1;

    @NotBlank(message = "Description should not be empty!")
    @Size(message = "The description should be shorter than 500 characters but longer than 3", max = 500, min = 3)
    private String description;

    @Min(value = 1, message = "At least 1 sq meter is needed")
    private int size = 5;

    @NotBlank(message = "Neighbourhood should not be empty!")
    @Size(message = "The neighbourhood should be shorter than 40 characters but longer than 3", max = 40, min = 3)
    private String neighbourhood;

    private int suggestedPrice = 0;

    private List<UserDto> users;
    private List<OfferDto> offers;

}
