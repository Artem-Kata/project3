package com.bank.authorization.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class LoginRequestDTO {

    @NotBlank(message = "Profile ID is required")
    private String profileId;

    @NotBlank(message = "Password is required")
    private String password;
}
