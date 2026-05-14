package com.nod.backend.distributed_coding_genie.workspace_service.dto.member;

import com.nod.backend.distributed_coding_genie.common_lib.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull ProjectRole role) {
}
