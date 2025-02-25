package com.bank.authorization.Aspect;

import com.bank.authorization.DTO.AuditDTO;
import com.bank.authorization.Entities.Audit;
import com.bank.authorization.Repositories.AuditRepository;
import com.bank.authorization.Repositories.UserRepository;
import com.bank.authorization.Services.Impl.AuditServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditServiceImpl auditServiceImpl;
    private final UserRepository userRepository;
    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        final String operationType = auditable.operationType();
        final String entityType = auditable.entityType();
        final Object result;
        String oldEntityJson = null;
        Long primaryKey = null;

        try {
            result = joinPoint.proceed();
            if ("CREATE".equalsIgnoreCase(operationType)) {
                logCreateAudit(entityType, result);
            } else if ("UPDATE".equalsIgnoreCase(operationType)) {
                primaryKey = extractIdFromResult(result);
                oldEntityJson = fetchOldEntityJson(primaryKey, entityType);
                logUpdateAudit(entityType, result, oldEntityJson, primaryKey);
            }
            return result;
        } catch (Exception e) {
            log.error("Audit error: {}", e.getMessage());
            throw new DataAccessException("Database operation failed", e) {
            };
        }
    }

    public void logCreateAudit(String entityType, Object result) {
        final String entityJson = convertToJson(result);

        final AuditDTO auditDTO = new AuditDTO();
        auditDTO.setEntityType(entityType);
        auditDTO.setOperationType("CREATE");
        auditDTO.setCreatedBy(String.valueOf(extractIdFromResult(result)));
        auditDTO.setCreatedAt(OffsetDateTime.now());
        auditDTO.setEntityJson(entityJson);
        auditDTO.setNewEntityJson(null);

        auditServiceImpl.createAudit(auditDTO);
    }

    private void logUpdateAudit(String entityType, Object result, String oldEntityJson, Long primaryKey) {
        final String newEntityJson = convertToJson(result);

        final AuditDTO auditDTO = new AuditDTO();
        auditDTO.setEntityType(entityType);
        auditDTO.setOperationType("UPDATE");
        auditDTO.setModifiedBy(String.valueOf(extractIdFromResult(result)));
        auditDTO.setModifiedAt(OffsetDateTime.now());
        auditDTO.setEntityJson(oldEntityJson);
        auditDTO.setNewEntityJson(newEntityJson);

        final Audit existingAudit = fetchAuditRecord(primaryKey, entityType);
        if (existingAudit != null) {
            auditDTO.setCreatedBy(existingAudit.getCreatedBy());
            auditDTO.setCreatedAt(existingAudit.getCreatedAt());
            auditServiceImpl.updateAudit(existingAudit.getId(), auditDTO);
        } else {
            auditDTO.setCreatedBy(String.valueOf(extractIdFromResult(result)));
            auditDTO.setCreatedAt(OffsetDateTime.now());
            auditServiceImpl.createAudit(auditDTO);
        }
    }

    private Long extractIdFromResult(Object result) {
        if (result instanceof ResponseEntity) {
            final Object body = ((ResponseEntity<?>) result).getBody();
            if (body != null) {
                result = body;
            } else {
                throw new IllegalArgumentException("ResponseEntity body is null");
            }
        }
        try {
            final Field idField = result.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return (Long) idField.get(result);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("ID field not found in result", e);
        }
    }

    private String fetchOldEntityJson(Long id, String entityType) {
        if ("User".equalsIgnoreCase(entityType)) {
            return userRepository.findById(id)
                    .map(this::convertToJson)
                    .orElse("{}");
        }
        return "{}";
    }

    private Audit fetchAuditRecord(Long id, String entityType) {
        final String searchStr = "\"id\":" + id;
        final Optional<Audit> auditOpt = auditRepository
                .findTopByEntityTypeAndEntityJsonContainingOrderByCreatedAtAsc(entityType, searchStr);
        return auditOpt.orElse(null);
    }

    private String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("JSON conversion error: {}", e.getMessage());
            return "{}";
        }
    }
}
