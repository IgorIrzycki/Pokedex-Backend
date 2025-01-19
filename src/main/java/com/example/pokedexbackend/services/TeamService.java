package com.example.pokedexbackend.services;

import com.example.pokedexbackend.dto.CreateTeamRequest;
import com.example.pokedexbackend.models.User;
import com.example.pokedexbackend.repositories.TeamRepository;
import com.example.pokedexbackend.models.Team;
import com.example.pokedexbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public void createTeam(CreateTeamRequest request) {

        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        Team team = new Team();
        team.setTeamName(request.getTeamName());
        team.setPokemonNames(request.getPokemonNames());
        team.setPokemonSprites(request.getPokemonSprites());


        Team savedTeam = teamRepository.save(team);


        List<Team> userTeams = user.getTeamIds() != null ? user.getTeamIds() : new ArrayList<>();
        userTeams.add(savedTeam);
        user.setTeamIds(userTeams);


        userRepository.save(user);
    }

    public void deleteTeamById(String id) {

        Team team = teamRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new IllegalArgumentException("Team with id " + id + " not found"));


        teamRepository.delete(team);


        User user = userRepository.findByTeamIdsContaining(team)
                .orElseThrow(() -> new IllegalArgumentException("Associated user not found"));

        List<Team> updatedTeams = user.getTeamIds();
        updatedTeams.remove(team);
        user.setTeamIds(updatedTeams);
        userRepository.save(user);
    }


    public void updateTeamById(String id, Team updatedTeam) {

        Team existingTeam = teamRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new IllegalArgumentException("Team with id " + id + " not found"));


        existingTeam.setTeamName(updatedTeam.getTeamName());
        existingTeam.setPokemonNames(updatedTeam.getPokemonNames());
        existingTeam.setPokemonSprites(updatedTeam.getPokemonSprites());
        teamRepository.save(existingTeam);


        User user = userRepository.findByTeamIdsContaining(existingTeam)
                .orElseThrow(() -> new IllegalArgumentException("Associated user not found"));

        List<Team> userTeams = user.getTeamIds();
        userTeams.removeIf(team -> team.getId().equals(existingTeam.getId()));
        userTeams.add(existingTeam);
        user.setTeamIds(userTeams);
        userRepository.save(user);
    }

}


