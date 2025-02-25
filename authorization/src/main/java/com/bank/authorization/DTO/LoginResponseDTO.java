package com.bank.authorization.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginResponseDTO {

    private Long profileId;
    private String token;
}
