package com.limtic.lab.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // ------------------ CONFIG ------------------
    private final String SECRET_BASE64 = "M31tO0Z1c3l1b1R1c1l1M31tO0Z1c3l1b1R1c1l1M31tO0Z1c3l1b1R1c1l1M31tO0Z1c3l1b1R1c1l1M31tO0Z1c3l1b1R1c1l1M31tO0Z1c3l1b1R1c1l1"; // Replace with a secure Base64 key

    private final Key SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET_BASE64));
    private final long EXPIRATION = 1000L * 60 * 60 * 24; // 24 hours in milliseconds

    // ------------------ GENERATE TOKEN ------------------
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    // ------------------ EXTRACT EMAIL ------------------
    public String getEmailFromToken(String token) {
        return parseToken(token).getBody().getSubject();
    }

    // ------------------ EXTRACT ROLE ------------------
    public String getRoleFromToken(String token) {
        return (String) parseToken(token).getBody().get("role");
    }

    // ------------------ VALIDATE TOKEN ------------------
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException |
                UnsupportedJwtException | IllegalArgumentException e) {
            System.err.println("JWT validation error: " + e.getMessage());
            return false;
        }
    }


    // ------------------ PRIVATE PARSE ------------------
    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
    }
}
