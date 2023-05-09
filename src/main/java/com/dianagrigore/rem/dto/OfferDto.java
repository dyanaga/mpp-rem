package com.dianagrigore.rem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferDto {

    private String offerId;

    @Min(value = 1, message = "At least 1 euro")
    private int price = 1;

    @NotBlank(message = "Comment should not be empty!")
    @Size(message = "The comment should be shorter than 500 characters but longer than 3", max = 500, min = 3)
    private String comment;
    private Date timestamp;

    private String userId;
    private String listingId;
    private ListingDto listing;
    private UserDto user;
}
