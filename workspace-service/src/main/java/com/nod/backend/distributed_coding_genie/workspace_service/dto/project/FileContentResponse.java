package com.nod.backend.distributed_coding_genie.workspace_service.dto.project;

public record FileContentResponse(
        String path,
        String content
) {
}
