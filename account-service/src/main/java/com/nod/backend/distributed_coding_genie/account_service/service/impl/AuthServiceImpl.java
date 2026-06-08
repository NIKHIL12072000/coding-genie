package com.nod.backend.distributed_coding_genie.account_service.service.impl;

import com.nod.backend.distributed_coding_genie.account_service.dto.auth.AuthResponse;
import com.nod.backend.distributed_coding_genie.account_service.dto.auth.LoginRequest;
import com.nod.backend.distributed_coding_genie.account_service.dto.auth.SignupRequest;
import com.nod.backend.distributed_coding_genie.account_service.entity.User;
import com.nod.backend.distributed_coding_genie.account_service.mapper.UserMapper;
import com.nod.backend.distributed_coding_genie.account_service.repository.UserRepository;
import com.nod.backend.distributed_coding_genie.account_service.service.AuthService;
import com.nod.backend.distributed_coding_genie.common_lib.error.BadRequestException;
import com.nod.backend.distributed_coding_genie.common_lib.security.AuthUtil;
import com.nod.backend.distributed_coding_genie.common_lib.security.JwtUserPrincipal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    AuthUtil authUtil;
    AuthenticationManager authenticationManager;

    @Override
    public AuthResponse signup(SignupRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("User already exists with username: " + request.username());
        });

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        JwtUserPrincipal jwtUserPrincipal = new JwtUserPrincipal(user.getId(), user.getName(),
                user.getUsername(), null, new ArrayList<>());

        String accessToken = authUtil.generateAccessToken(jwtUserPrincipal);
        String refreshToken = authUtil.generateRefreshToken(jwtUserPrincipal);
        return new AuthResponse(accessToken, refreshToken, userMapper.toUserProfileResponse(jwtUserPrincipal));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        JwtUserPrincipal user = (JwtUserPrincipal) authentication.getPrincipal();
        String accessToken = authUtil.generateAccessToken(user);
        String refreshToken = authUtil.generateRefreshToken(user);
        
        return new AuthResponse(accessToken, refreshToken, userMapper.toUserProfileResponse(user));
    }

    @Override
    public AuthResponse refreshToken(String refreshToken){
        if (refreshToken == null) {
            throw new BadRequestException("Missing refresh token");
        }

        JwtUserPrincipal user = authUtil.verifyRefreshToken(refreshToken);

        String newAccessToken = authUtil.generateAccessToken(user);

        return new AuthResponse(newAccessToken, refreshToken,
                userMapper.toUserProfileResponse(user));
    }
}
