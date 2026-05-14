package com.nod.backend.distributed_coding_genie.workspace_service.dto.project;


import com.nod.backend.distributed_coding_genie.common_lib.enums.ProjectRole;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        ProjectRole role
) {
}
