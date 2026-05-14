package com.nod.backend.distributed_coding_genie.intelligence_service.dto.chat;


import com.nod.backend.distributed_coding_genie.common_lib.enums.MessageRole;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        MessageRole role,
        List<ChatEventResponse> events,
        String content,
        Integer tokensUsed,
        Instant createdAt

) {
}
