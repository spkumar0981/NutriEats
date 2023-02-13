package com.nutri.rest.repository;

import com.nutri.rest.model.AuditLogging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLoggingRepository extends JpaRepository<AuditLogging, Long> {
    AuditLogging findByToken(String token);

    List<AuditLogging> findByUserNameAndLogoutTimeIsNull(String username);
}
