package com.nod.backend.distributed_coding_genie.intelligence_service.mapper;

import com.nod.backend.distributed_coding_genie.intelligence_service.dto.chat.ChatResponse;
import com.nod.backend.distributed_coding_genie.intelligence_service.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessageList);
}
