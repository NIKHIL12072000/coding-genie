package com.nod.backend.distributed_coding_genie.intelligence_service.repository;

import com.nod.backend.distributed_coding_genie.intelligence_service.entity.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UsageLogRepository extends JpaRepository<UsageLog, Long> {
    Optional<UsageLog> findByUserIdAndDate(Long userId, LocalDate today);
}
