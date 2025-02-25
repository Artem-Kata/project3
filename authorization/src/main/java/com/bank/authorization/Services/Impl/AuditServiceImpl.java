package com.bank.authorization.Services.Impl;

import com.bank.authorization.DTO.AuditDTO;
import com.bank.authorization.Entities.Audit;
import com.bank.authorization.Mapper.EntityMapper;
import com.bank.authorization.Repositories.AuditRepository;
import com.bank.authorization.Services.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Transactional
    public void createAudit(AuditDTO auditDTO) {
        log.info("Saving audit record for entity: {}", auditDTO.getEntityType());
        final Audit audit = EntityMapper.toAudit(auditDTO);
        auditRepository.save(audit);
    }

    @Transactional
    public void updateAudit(Long id, AuditDTO auditDTO) {
        log.info("Updating audit record with id: {}", id);
        final Audit existAudit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit record not found with id: " + id));
        // - operation_type, чтобы поменять с CREATE на UPDATE
        existAudit.setOperationType(auditDTO.getOperationType());
        existAudit.setModifiedBy(auditDTO.getModifiedBy());
        existAudit.setModifiedAt(auditDTO.getModifiedAt());
        existAudit.setNewEntityJson(auditDTO.getNewEntityJson());
        auditRepository.save(existAudit);
    }
}
