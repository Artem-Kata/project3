package com.bank.authorization.Services.Impl;

import com.bank.authorization.DTO.UserDTO;
import com.bank.authorization.Entities.User;
import com.bank.authorization.Exceptions.EntityNotFoundException;
import com.bank.authorization.Mapper.EntityMapper;
import com.bank.authorization.Repositories.UserRepository;
import com.bank.authorization.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        final User user = EntityMapper.toUser(userDTO);
        final User savedUser = userRepository.save(user);
        log.info("User created with profileId: {}", savedUser.getProfileId());
        return EntityMapper.toUserDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        final User oldUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + id));
        if (userDTO.getRole() != null) {
            oldUser.setRole(userDTO.getRole());
        }
        if (userDTO.getProfileId() != null) {
            oldUser.setProfileId(userDTO.getProfileId());
        }
        if (userDTO.getPassword() != null && !passwordEncoder.matches(userDTO.getPassword(), oldUser.getPassword())) {
            oldUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        final User updatedUser = userRepository.save(oldUser);
        log.info("User updated with id: {}", updatedUser.getId());
        return EntityMapper.toUserDTO(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        final User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + id));
        log.info("User retrieved with id: {}", id);
        return EntityMapper.toUserDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findByProfileId(Long profileId) {
        final User user = userRepository.findByProfileId(profileId).orElseThrow(() ->
                new EntityNotFoundException("User not found with profileId: " + profileId));
        log.info("User retrieved with profileId: {}", profileId);
        return EntityMapper.toUserDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        log.info("Retrieving all users");
        return userRepository.findAll().stream()
                .map(EntityMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long profileId) {
        final User deleteUser = userRepository.findByProfileId(profileId).orElseThrow(() ->
                new EntityNotFoundException("User not found with profileId: " + profileId));
        userRepository.delete(deleteUser);
        log.info("User deleted with profileId: {}", profileId);
    }
}
