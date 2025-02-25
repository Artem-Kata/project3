package com.bank.authorization.Services;

import com.bank.authorization.DTO.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    UserDTO getUserById(Long id);

    UserDTO findByProfileId(Long profileId);

    List<UserDTO> getAllUsers();

    void deleteUser(Long profileId);
}
