package com.bank.authorization.Services;

import com.bank.authorization.DTO.LoginRequestDTO;
import com.bank.authorization.DTO.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO loginRequest);
}
