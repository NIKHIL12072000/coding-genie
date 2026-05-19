package com.nod.backend.distributed_coding_genie.intelligence_service.controller;

import com.nod.backend.distributed_coding_genie.intelligence_service.dto.chat.ChatRequest;
import com.nod.backend.distributed_coding_genie.intelligence_service.dto.chat.ChatResponse;
import com.nod.backend.distributed_coding_genie.intelligence_service.dto.chat.StreamResponse;
import com.nod.backend.distributed_coding_genie.intelligence_service.service.AiGenerationService;
import com.nod.backend.distributed_coding_genie.intelligence_service.service.ChatService;
import com.nod.backend.distributed_coding_genie.intelligence_service.security.SecurityExpressions;
import org.springframework.security.access.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final AiGenerationService aiGenerationService;
    private final ChatService chatService;
    private final SecurityExpressions securityExpressions;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<StreamResponse>> streamChat(
            @RequestBody ChatRequest request) {

        if (!securityExpressions.canEditProject(request.projectId())) {
            throw new AccessDeniedException("Insufficient permissions for this project");
        }

        return aiGenerationService.streamResponse(request.message(), request.projectId())
                .map(data -> ServerSentEvent.<StreamResponse>builder()
                        .data(data)
                        .build());
    }

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<List<ChatResponse>> getChatHistory(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(chatService.getProjectChatHistory(projectId));
    }
}
