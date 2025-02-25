package com.bank.authorization.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.time.OffsetDateTime;

@Entity
@Table(name = "audit", schema = "\"authorization\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entity_type", nullable = false, length = 40)
    private String entityType;

    @Column(name = "operation_type", nullable = false, length = 255)
    private String operationType;

    @Column(name = "created_by", nullable = false, length = 255)
    private String createdBy;

    @Column(name = "modified_by", length = 255)
    private String modifiedBy;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "new_entity_json", columnDefinition = "TEXT")
    private String newEntityJson;

    @Column(name = "entity_json", nullable = false, columnDefinition = "TEXT")
    private String entityJson;

}
