package com.example.pokedexbackend.controllers;

import com.example.pokedexbackend.dto.LoginRequest;
import com.example.pokedexbackend.dto.LoginResponse;
import com.example.pokedexbackend.dto.RegisterRequest;
import com.example.pokedexbackend.dto.UserDTO;
import com.example.pokedexbackend.models.User;
import com.example.pokedexbackend.services.JwtService;
import com.example.pokedexbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getUserName(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = null;
        try {
            User user = userService.authenticateUser(request.getUserName(), request.getPassword());
            System.out.println("User authenticated");
            token = jwtService.generateToken(user);
            System.out.println("token generated: " + token);
            return ResponseEntity.ok(new LoginResponse(request.getUserName(), token));
        } catch (Exception e) {
            System.out.println(token);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userName}")
    public ResponseEntity<?> getUser(@PathVariable String userName) {
        try {
            UserDTO userDTO = userService.getUserByUserNameWithTeams(userName);
            return ResponseEntity.ok(userDTO);  // Zwróć DTO z ID w formacie String
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
