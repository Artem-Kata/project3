package com.bank.authorization.Controllers;

import com.bank.authorization.DTO.LoginRequestDTO;
import com.bank.authorization.DTO.LoginResponseDTO;
import com.bank.authorization.Services.Impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@Tag(name = "Authentication", description = "API for user authentication")
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @Autowired
    public AuthController(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Returns the JWT token for authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful authentication"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "The user was not found")
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Login attempt for profileId: {}", loginRequest.getProfileId());
        try {
            final LoginResponseDTO response = authServiceImpl.login(loginRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
