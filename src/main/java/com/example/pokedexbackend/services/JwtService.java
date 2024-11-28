package com.example.pokedexbackend.services;

import com.example.pokedexbackend.models.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generowanie tokenu
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key)
                .compact();
    }

    // Weryfikacja tokenu
    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // Parsowanie tokenu i sprawdzanie podpisu
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired", e);  // Obsługuje wygasły token
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e);  // Obsługuje nieprawidłowy token
        }
    }

    // Sprawdzanie, czy token jest wygasły
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (JwtException e) {
            return true;  // Jeśli nie udało się zweryfikować tokenu, traktujemy go jako wygasły
        }
    }
}


