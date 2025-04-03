package com.banking.security;

import com.banking.repository.TokenRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromHeader(request);

        if (token != null) {
            Optional<com.banking.model.Token> tokenEntry = tokenRepository.findByToken(token);

            if (tokenEntry.isPresent() && tokenEntry.get().getExpiry().isAfter(java.time.LocalDateTime.now()) &&
                    jwtUtil.validateToken(token, jwtUtil.extractUsername(token))) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        jwtUtil.extractUsername(token), null, new ArrayList<>()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("🔍 Incoming Request - Authorization Header: " + authHeader);
        return (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }
}
