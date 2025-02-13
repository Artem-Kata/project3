package com.bank.authorization.Services;

import com.bank.authorization.DTO.LoginRequestDTO;
import com.bank.authorization.DTO.LoginResponseDTO;
import com.bank.authorization.Entities.User;
import com.bank.authorization.Exceptions.AuthenticationException;
import com.bank.authorization.Repositories.UserRepository;
import com.bank.authorization.Security.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JWTTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, JWTTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // Аутентификация с помощью AuthenticationManager
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getProfileId(), loginRequest.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid credentials");
        }

        // Загрузка пользователя
        User user = userRepository.findByProfileId(Long.parseLong(loginRequest.getProfileId()))
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // Генерация JWT-токена
        String token = tokenProvider.generateToken(String.valueOf(user.getProfileId()));
        return new LoginResponseDTO(token);
    }
}
