package com.nod.backend.distributed_coding_genie.intelligence_service.repository;

import com.nod.backend.distributed_coding_genie.intelligence_service.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {
    Optional<ChatEvent> findBySagaId(String s);
}
