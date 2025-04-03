package com.banking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b"; // Must be at least 256 bits

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // ✅ Correct signing method
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder() // ✅ Use parserBuilder() instead of parser()
                .setSigningKey(getSigningKey()) // ✅ Use setSigningKey() instead of verifyWith()
                .build()
                .parseClaimsJws(token) // ✅ Use parseClaimsJws() instead of parseSignedClaims()
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            return extractUsername(token).equals(username);
        } catch (JwtException e) {
            return false;
        }
    }
}
