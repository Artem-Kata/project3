package com.bank.authorization.Controllers;

import com.bank.authorization.DTO.AuditDTO;
import com.bank.authorization.Services.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/authorization/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    public AuditDTO createAudit(@RequestBody AuditDTO auditDTO) {
        log.info("Creating audit for entity type: {}", auditDTO.getEntityType());
        return auditService.createAudit(auditDTO);
    }

    /*@PatchMapping("/{id}")
    public AuditDTO updateAudit(@PathVariable Long id, @RequestBody AuditDTO auditDTO) {
        return auditService.updateAudit(id, auditDTO);
    }*/
}
