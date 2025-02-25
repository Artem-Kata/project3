package com.bank.authorization.Controllers;

import com.bank.authorization.Aspect.Auditable;
import com.bank.authorization.DTO.UserDTO;
import com.bank.authorization.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;

@Slf4j
@RestController
@Tag(name = "User Management", description = "API for user management")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Auditable(operationType = "CREATE", entityType = "User")
    @Operation(summary = "User Registration", description = "Creates a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "The user was successfully created"),
            @ApiResponse(responseCode = "400", description = "Incorrect data")
    })
    public ResponseEntity<?> createUser(@Validated(UserDTO.CreateValidationGroup.class) @RequestBody UserDTO userDTO) {
        log.info("Creating user with profileId: {}", userDTO.getProfileId());
        final UserDTO createdUser = userService.createUser(userDTO);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{profileId}")
    @PreAuthorize("#profileId.toString() == authentication.name")
    @Operation(summary = "Get a user", description = "Returns user data by profileId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user has been found"),
            @ApiResponse(responseCode = "404", description = "The user was not found"),
            @ApiResponse(responseCode = "500", description = "Access is denied")
    })
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long profileId) {
        log.info("Getting user with profileId: {}", profileId);
        final UserDTO user = userService.findByProfileId(profileId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/{profileId}")
    @PreAuthorize("#profileId.toString() == authentication.name")
    @Auditable(operationType = "UPDATE", entityType = "User")
    @Operation(summary = "Updating user data", description = "Updates user data by profileId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The user has been updated"),
            @ApiResponse(responseCode = "404", description = "The user was not found")
    }
    )
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long profileId, @Valid @RequestBody UserDTO userDTO) {
        log.info("Updating user with profileId: {}", profileId);
        final UserDTO oldUser = userService.findByProfileId(profileId);
        final UserDTO updatedUser = userService.updateUser(oldUser.getId(), userDTO);
        if (updatedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{profileId}")
    @PreAuthorize("#profileId.toString() == authentication.name")
    @Operation(summary = "Deleting a user", description = "Deletes user data by profileId")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "The user has been delete")
    }
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long profileId) {
        log.info("Deleting user with profileId: {}", profileId);
        userService.deleteUser(profileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
