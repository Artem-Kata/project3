package com.bank.authorization.Repositories;

import com.bank.authorization.Entities.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    Optional<Audit> findTopByEntityTypeAndEntityJsonContainingOrderByCreatedAtAsc(String entityType, String content);
}
