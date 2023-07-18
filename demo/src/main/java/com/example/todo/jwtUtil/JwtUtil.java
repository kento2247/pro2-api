package com.example.todo.jwtUtil;

import io.jsonwebtoken.Jwts;

import java.util.Date;

public class JwtUtil {
    private final String SECRET_KEY = "demo";

    public String extractNickname(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public Date extractExpiration(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)).signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractNickname(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}