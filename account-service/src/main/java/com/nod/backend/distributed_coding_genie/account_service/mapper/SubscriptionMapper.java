package com.nod.backend.distributed_coding_genie.account_service.mapper;

import com.nod.backend.distributed_coding_genie.account_service.dto.subscription.SubscriptionResponse;
import com.nod.backend.distributed_coding_genie.account_service.entity.Plan;
import com.nod.backend.distributed_coding_genie.account_service.entity.Subscription;
import com.nod.backend.distributed_coding_genie.common_lib.dto.PlanDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanDto toPlanResponse(Plan plan);
}
