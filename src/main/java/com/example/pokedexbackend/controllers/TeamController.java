package com.example.pokedexbackend.controllers;

import com.example.pokedexbackend.dto.CreateTeamRequest;
import com.example.pokedexbackend.models.User;
import com.example.pokedexbackend.repositories.TeamRepository;
import com.example.pokedexbackend.repositories.UserRepository;
import com.example.pokedexbackend.services.TeamService;
import com.example.pokedexbackend.models.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<String> createTeam(@RequestBody CreateTeamRequest request) {
        try {
            teamService.createTeam(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Team created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create team: " + e.getMessage());
        }
    }
}


