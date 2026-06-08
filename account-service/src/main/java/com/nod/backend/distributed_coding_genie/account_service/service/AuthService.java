package com.nod.backend.distributed_coding_genie.account_service.service;

import com.nod.backend.distributed_coding_genie.account_service.dto.auth.AuthResponse;
import com.nod.backend.distributed_coding_genie.account_service.dto.auth.LoginRequest;
import com.nod.backend.distributed_coding_genie.account_service.dto.auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(String refreshToken);
}
