package com.nod.backend.distributed_coding_genie.workspace_service.repository;

import com.nod.backend.distributed_coding_genie.workspace_service.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
}
