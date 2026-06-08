package com.nod.backend.distributed_coding_genie.account_service.dto.auth;

public record AuthResponse(
                String accessToken,
                String refreshToken,
                UserProfileResponse user) {

}