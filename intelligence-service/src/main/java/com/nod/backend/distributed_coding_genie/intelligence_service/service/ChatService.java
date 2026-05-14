package com.nod.backend.distributed_coding_genie.intelligence_service.service;


import com.nod.backend.distributed_coding_genie.intelligence_service.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);
}
