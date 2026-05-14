package com.nod.backend.distributed_coding_genie.workspace_service.dto.member;


import com.nod.backend.distributed_coding_genie.common_lib.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
