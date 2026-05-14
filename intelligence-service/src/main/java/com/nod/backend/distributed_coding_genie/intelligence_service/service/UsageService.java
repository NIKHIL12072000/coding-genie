package com.nod.backend.distributed_coding_genie.intelligence_service.service;

public interface UsageService {
    void recordTokenUsage(Long userId, int actualTokens);
    void checkDailyTokensUsage();
}
