package com.nod.backend.distributed_coding_genie.common_lib.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;   // Cookie handling (Jakarta Servlet API)
import java.util.Arrays;              // Arrays.stream(...)
import java.util.Optional;            // Optional.ofNullable(...)

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("Incoming Request: {}", request.getRequestURI());
            final String requestHeaderToken = request.getHeader("Authorization");
            if (requestHeaderToken == null || !requestHeaderToken.startsWith("Bearer ")) {
                response.sendRedirect("/login");
                return;
            }
            String jwtToken = requestHeaderToken.split("Bearer ")[1];
            JwtUserPrincipal user = authUtil.verifyAccessToken(jwtToken);
            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.authorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            Cookie[] cookies = request.getCookies();
            String refresh = Arrays.stream(Optional.ofNullable(cookies).orElse(new Cookie[0]))
                                .filter(c -> "refresh_token".equals(c.getName()))
                                .findFirst()
                                .map(Cookie::getValue)
                                .orElse(null);

            if (refresh != null) {
                try {
                    JwtUserPrincipal user = authUtil.verifyRefreshToken(refresh);
                    String newAccess = authUtil.generateAccessToken(user);
                    response.setHeader("Authorization", "Bearer " + newAccess);
                    UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(user, null, user.authorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    filterChain.doFilter(request, response);
                    return;
                } catch (JwtException re) {
                    // Refresh token also invalid – fall through to resolver
                }
            }
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
            catch (Exception e) {
                handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
