package com.nod.backend.distributed_coding_genie.workspace_service.service;

import com.nod.backend.distributed_coding_genie.workspace_service.dto.project.DeployResponse;
import org.jspecify.annotations.Nullable;

public interface DeploymentService {
    @Nullable DeployResponse deploy(Long projectId);
}
