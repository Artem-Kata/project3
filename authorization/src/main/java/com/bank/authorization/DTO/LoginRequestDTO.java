package com.bank.authorization.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Schema(description = "Login information")
public class LoginRequestDTO {

    @NotNull(message = "Profile ID is required")
    @Schema(description = "Profile ID", example = "123456789", required = true)
    private Long profileId;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "mySecurePassword", required = true)
    private String password;
}
