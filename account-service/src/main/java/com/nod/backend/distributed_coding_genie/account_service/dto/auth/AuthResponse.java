package com.nod.backend.distributed_coding_genie.account_service.dto.auth;

public record AuthResponse(
                String token,
                UserProfileResponse user) {

}
