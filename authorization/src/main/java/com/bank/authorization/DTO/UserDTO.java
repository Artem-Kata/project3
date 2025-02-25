package com.bank.authorization.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Schema(description = "User data")
public class UserDTO {

    private Long id;

    @NotBlank(groups = CreateValidationGroup.class, message = "Role is required")
    @Size(groups = CreateValidationGroup.class, max = 40)
    @Schema(description = "User's role", example = "USER")
    private String role;

    @NotNull(groups = CreateValidationGroup.class, message = "ProfileId is required")
    @Schema(description = "Profile Id", example = "123456789")
    private Long profileId;

    @NotBlank(groups = CreateValidationGroup.class, message = "Password is required")
    @Size(groups = CreateValidationGroup.class, max = 500)
    @Schema(description = "Password", example = "mysecret")
    private String password;

    public interface CreateValidationGroup {
    }
}
