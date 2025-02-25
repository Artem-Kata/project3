package com.bank.authorization.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

@Data
public class AuditDTO {

    private Long id;

    @NotBlank(message = "Entity type is required")
    @Size(max = 40, message = "EntityType must be less than 40 characters")
    private String entityType;

    @NotBlank(message = "Operation type is required")
    @Size(max = 255, message = "OperationType must be less than 255 characters")
    private String operationType;

    @NotBlank(groups = UserDTO.CreateValidationGroup.class, message = "Created by is required")
    @Size(max = 255, message = "CreatedBy must be less than 255 characters")
    private String createdBy;

    @Size(max = 255, message = "ModifiedBy must be less than 255 characters")
    private String modifiedBy;

    @NotNull(groups = UserDTO.CreateValidationGroup.class, message = "Created at is required")
    private OffsetDateTime createdAt;

    private OffsetDateTime modifiedAt;

    private String newEntityJson;

    @NotBlank(message = "Entity JSON is required")
    private String entityJson;
}
