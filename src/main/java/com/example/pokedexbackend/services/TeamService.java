package com.example.pokedexbackend.services;

import com.example.pokedexbackend.dto.CreateTeamRequest;
import com.example.pokedexbackend.models.User;
import com.example.pokedexbackend.repositories.TeamRepository;
import com.example.pokedexbackend.models.Team;
import com.example.pokedexbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public void createTeam(CreateTeamRequest request) {
        // Pobierz użytkownika po nazwie
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Utwórz nową drużynę
        Team team = new Team();
        team.setTeamName(request.getTeamName());
        team.setPokemonNames(request.getPokemonNames());
        team.setPokemonSprites(request.getPokemonSprites());

        // Zapisz drużynę do bazy danych
        Team savedTeam = teamRepository.save(team);

        // Powiąż drużynę z użytkownikiem
        List<Team> userTeams = user.getTeamIds() != null ? user.getTeamIds() : new ArrayList<>();
        userTeams.add(savedTeam);
        user.setTeamIds(userTeams);

        // Zaktualizuj użytkownika
        userRepository.save(user);
    }
}


