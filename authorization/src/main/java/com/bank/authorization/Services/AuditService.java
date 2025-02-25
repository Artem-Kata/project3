package com.bank.authorization.Services;

import com.bank.authorization.DTO.AuditDTO;

public interface AuditService {

    void createAudit(AuditDTO auditDTO);

    void updateAudit(Long id, AuditDTO auditDTO);
}
