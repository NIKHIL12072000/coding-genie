package com.nod.backend.distributed_coding_genie.account_service.controller;

import com.nod.backend.distributed_coding_genie.account_service.dto.auth.AuthResponse;
import com.nod.backend.distributed_coding_genie.account_service.dto.auth.LoginRequest;
import com.nod.backend.distributed_coding_genie.account_service.dto.auth.SignupRequest;
import com.nod.backend.distributed_coding_genie.account_service.service.AuthService;
import com.nod.backend.distributed_coding_genie.common_lib.security.JwtUserPrincipal;
import com.nod.backend.distributed_coding_genie.common_lib.security.AuthUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        AuthResponse authResponse=authService.signup(request);
        String refreshToken = authResponse.refreshToken();
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                      .httpOnly(true)
                      .sameSite("Strict")
                      .path("/")
                      .maxAge(Duration.ofDays(30))
                      .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse=authService.login(request);
        String refreshToken = authResponse.refreshToken();
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                      .httpOnly(true)
                      .sameSite("Strict")
                      .path("/")
                      .maxAge(Duration.ofDays(30))
                      .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(authResponse);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        if (!org.springframework.util.StringUtils.hasText(refreshToken)) {
            return ResponseEntity.status(401).build();
        }
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", authResponse.refreshToken())
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

}
