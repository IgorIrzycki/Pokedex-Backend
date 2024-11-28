package com.example.pokedexbackend.config;

import com.example.pokedexbackend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Pobranie tokenu z nagłówka Authorization
        String token = getTokenFromRequest(request);
        System.out.println(token);

        if (token != null && !token.isEmpty()) {
            try {
                // Weryfikacja tokenu
                String userName = jwtService.validateToken(token);

                // Stworzenie obiektu autoryzacji na podstawie nazwy użytkownika
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication); // Ustawienie autoryzacji w kontekście
            } catch (RuntimeException e) {
                // Obsługa błędów w przypadku nieprawidłowego lub wygasłego tokenu
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        }

        System.out.println("Success!!!");
        filterChain.doFilter(request, response); // Kontynuowanie przetwarzania żądania
    }

    // Pomocnicza metoda do pobierania tokenu z nagłówka Authorization
    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Usuwamy "Bearer " z nagłówka
        }
        return null;
    }
}


