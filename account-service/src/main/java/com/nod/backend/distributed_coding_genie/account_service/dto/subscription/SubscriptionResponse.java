package com.nod.backend.distributed_coding_genie.account_service.dto.subscription;

import com.nod.backend.distributed_coding_genie.common_lib.dto.PlanDto;

import java.time.Instant;

public record SubscriptionResponse(
        PlanDto plan,
        String status,
        Instant currentPeriodEnd,
        Long tokensUsedThisCycle
) {
}
