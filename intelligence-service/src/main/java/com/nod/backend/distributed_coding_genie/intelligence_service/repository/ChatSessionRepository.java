package com.nod.backend.distributed_coding_genie.intelligence_service.repository;

import com.nod.backend.distributed_coding_genie.intelligence_service.entity.ChatSession;
import com.nod.backend.distributed_coding_genie.intelligence_service.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
