package com.dianagrigore.rem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDto {

    private String offerId;

    @Min(value = 1, message = "At least 1 star")
    @Max(value = 5, message = "Max 5 stars")
    private int stars = 1;

    @NotBlank(message = "Comment should not be empty!")
    @Size(message = "The comment should be shorter than 500 characters but longer than 3", max = 500, min = 3)
    private String review;

    private Date timestamp;

    private UserDto user;
    private UserDto creatorUser;
}
