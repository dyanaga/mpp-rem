package com.dianagrigore.rem.dto;

import com.dianagrigore.rem.model.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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

    @Size(message = "The mail should be shorter than 100 characters", max = 100)
    @Pattern(message = "Invalid email format!", regexp = "((?:^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]+$)|^$)")
    private String email;

    @Size(message = "The password should be shorter than 100 characters", max = 100)
    private String password;

    @Size(message = "The phone number should be shorter than 20 characters", max = 20)
    @Pattern(regexp = "(^(?:\\+407|0[1-9])[0-9]{8}$|^$)", message = "Wrong format for the phone number")
    private String phoneNumber;

    @NotNull
    private UserType type;

    private List<OfferDto> offers;

}
