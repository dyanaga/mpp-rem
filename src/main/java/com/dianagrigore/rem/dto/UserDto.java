package com.dianagrigore.rem.dto;

import com.dianagrigore.rem.model.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String userId;

    @NotBlank(message = "Name should not be empty!")
    @Size(message = "The name should be shorter than 100 characters but longer than 3", max = 100, min = 3)
    private String name;

    @NotBlank(message = "Name should not be empty!")
    @Size(message = "The username should be shorter than 50 characters but longer than 5", max = 50, min = 5)
    private String username;

    @Size(message = "The mail should be shorter than 100 characters", max = 100)
    @Pattern(message = "Invalid email format!", regexp = "((?:^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]+$)|^$)")
    private String email;

    @Size(message = "The password length should be strong and between 8 and 12 characters", max = 12, min = 8)
    @Pattern(regexp = "^(?:(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}|)$", message = "Password not strong enough.")
    private String password;

    @Size(message = "The phone number should be shorter than 20 characters", max = 20)
    @Pattern(regexp = "(^(?:\\+407|0[1-9])[0-9]{8}$|^$)", message = "Wrong format for the phone number")
    private String phoneNumber;

    @Size(message = "The bio should be shorter than 500 characters", max = 500)
    private String bio;

    @Size(message = "The location should be shorter than 100 characters", max = 100)
    @NotBlank(message = "The location should not be empty")
    private String location;

    @Size(message = "The gender should be shorter than 100 characters", max = 100)
    private String gender;

    private Date birthday;

    @Min(message = "Size has to be between 5 and 100", value = 5)
    @Max(message = "Size has to be between 5 and 100", value = 100)
    private int pagePreference = 10;

    @Size(message = "The url should be shorter than 100 characters", max = 100)
    private String url;

    @NotNull
    private UserType type;

    private List<OfferDto> offers;
    private List<ReviewDto> reviews;
    private List<ReviewDto> writtenReviews;
    private List<ListingDto> listingsCreated;
    private List<ListingDto> listings;

}
