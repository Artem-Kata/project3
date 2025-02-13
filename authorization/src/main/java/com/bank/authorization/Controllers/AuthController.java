package com.bank.authorization.Controllers;

import com.bank.authorization.DTO.LoginRequestDTO;
import com.bank.authorization.DTO.LoginResponseDTO;
import com.bank.authorization.Services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/authorization/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Login attempt for profileId: {}", loginRequest.getProfileId());
        return authService.login(loginRequest);
    }
}
