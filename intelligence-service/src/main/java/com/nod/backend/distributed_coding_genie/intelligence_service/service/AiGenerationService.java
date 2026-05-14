package com.nod.backend.distributed_coding_genie.intelligence_service.service;

import com.nod.backend.distributed_coding_genie.intelligence_service.dto.chat.StreamResponse;
import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<StreamResponse> streamResponse(String message, Long projectId);
}
