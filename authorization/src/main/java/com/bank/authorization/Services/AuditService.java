package com.bank.authorization.Services;

import com.bank.authorization.DTO.AuditDTO;
import com.bank.authorization.Entities.Audit;
import com.bank.authorization.Mapper.EntityMapper;
import com.bank.authorization.Repositories.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditRepository auditRepository;

    @Autowired
    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public AuditDTO createAudit(AuditDTO auditDTO) {
        Audit audit = EntityMapper.toAudit(auditDTO);
        if (audit.getCreatedAt() == null) {
            audit.setCreatedAt(java.time.OffsetDateTime.now());
        }
        Audit savedAudit = auditRepository.save(audit);
        return EntityMapper.toAuditDTO(savedAudit);
    }
}
