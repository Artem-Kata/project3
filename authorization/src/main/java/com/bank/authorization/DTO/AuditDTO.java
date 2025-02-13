package com.bank.authorization.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Data
public class AuditDTO {

    private Long id;

    @NotBlank(message = "Entity type is required")
    private String entityType;

    @NotBlank(message = "Operation type is required")
    private String operationType;

    @NotBlank(message = "Created by is required")
    private String createdBy;

    private String modifiedBy;

    @NotNull(message = "Created at is required")
    private OffsetDateTime createdAt;

    private OffsetDateTime modifiedAt;

    @NotBlank(message = "New entity JSON is required")
    private String newEntityJson;

    @NotBlank(message = "Entity JSON is required")
    private String entityJson;
}
