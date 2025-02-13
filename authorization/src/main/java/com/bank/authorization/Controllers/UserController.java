package com.bank.authorization.Controllers;

import com.bank.authorization.DTO.UserDTO;
import com.bank.authorization.Entities.User;
import com.bank.authorization.Mapper.EntityMapper;
import com.bank.authorization.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/authorization/users")
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        // Проверяем, не существует ли пользователь с таким profileId
        Optional<User> existingUser = userRepository.findByProfileId(userDTO.getProfileId());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with profileId " + userDTO.getProfileId() + " already exists.");
        }

        // Преобразуем DTO в сущность
        User user = EntityMapper.toUser(userDTO);
        // Шифруем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Сохраняем пользователя
        User savedUser = userRepository.save(user);
        log.info("Registered new user with profileId: {}", savedUser.getProfileId());
        return ResponseEntity.status(HttpStatus.CREATED).body(EntityMapper.toUserDTO(savedUser));
    }
}
