package com.bank.authorization.Controllers;

import com.bank.authorization.Aspect.Auditable;
import com.bank.authorization.DTO.UserDTO;
import com.bank.authorization.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Admin Management", description = "API for administrative management")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/admin/users")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get a user", description = "Requires the ADMIN role")
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Requires the ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users"),
            @ApiResponse(responseCode = "403", description = "Access is denied")
    })
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.info("Getting all users");
        final List<UserDTO> users = userService.getAllUsers();
        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PatchMapping("/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Auditable(operationType = "UPDATE", entityType = "User")
    @Operation(summary = "Updating user data", description = "Requires the ADMIN role")
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deleting a user", description = "Requires the ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "The user has been deleted")
    }
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long profileId) {
        log.info("Deleting user with profileId: {}", profileId);
        userService.deleteUser(profileId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
