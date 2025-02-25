package com.bank.authorization.Services.Impl;

import com.bank.authorization.DTO.LoginRequestDTO;
import com.bank.authorization.DTO.LoginResponseDTO;
import com.bank.authorization.Entities.User;
import com.bank.authorization.Exceptions.AuthenticationException;
import com.bank.authorization.Exceptions.EntityNotFoundException;
import com.bank.authorization.Repositories.UserRepository;
import com.bank.authorization.Security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements com.bank.authorization.Services.AuthService {

    private final UserRepository userRepository;
    private final JWTTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           JWTTokenProvider tokenProvider,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getProfileId(), loginRequest.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid credentials");
        }

        final User user = userRepository.findByProfileId(loginRequest.getProfileId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        final String token = tokenProvider.generateToken(String.valueOf(user.getProfileId()));
        return new LoginResponseDTO(loginRequest.getProfileId(), token);
    }
}
