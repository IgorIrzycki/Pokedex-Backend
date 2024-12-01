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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeamById(@PathVariable String id) {
        try {
            teamService.deleteTeamById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Team deleted successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete team: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeamById(@PathVariable String id, @RequestBody Team updatedTeam) {
        try {
            teamService.updateTeamById(id, updatedTeam);
            return ResponseEntity.status(HttpStatus.OK).body("Team updated successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to update team: " + e.getMessage());
        }
    }


}


