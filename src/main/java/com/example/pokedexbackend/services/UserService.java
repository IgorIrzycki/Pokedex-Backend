package com.example.pokedexbackend.services;

import com.example.pokedexbackend.dto.TeamDTO;
import com.example.pokedexbackend.dto.UserDTO;
import com.example.pokedexbackend.models.User;
import com.example.pokedexbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String userName, String email, String password) {
        if (userRepository.findByUserName(userName).isPresent()) {
            throw new IllegalStateException("Username is already taken.");
        }

        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setTeamIds(new ArrayList<>()); // Pusta lista drużyn

        return userRepository.save(user);
    }

    public User authenticateUser(String userName, String password) {
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if (optionalUser.isEmpty() || !passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            throw new IllegalStateException("Invalid username or password.");
        }
        return optionalUser.get();
    }

    public User getUserByUserName(String userName) {
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found.");
        }
        return optionalUser.get();  // Zwracamy użytkownika z drużynami (wraz z @DocumentReference)
    }

    public UserDTO getUserByUserNameWithTeams(String userName) {
        User user = getUserByUserName(userName); // Pobierz użytkownika z bazy danych

        List<TeamDTO> teamDTOs = user.getTeamIds().stream().map(team ->
                new TeamDTO(
                        team.getId().toHexString(),  // Konwersja ObjectId na String
                        team.getTeamName(),
                        team.getPokemonNames(),
                        team.getPokemonSprites()
                )
        ).toList();

        return new UserDTO(
                user.getId().toHexString(), // Konwersja ObjectId użytkownika na String
                user.getUserName(),
                user.getEmail(),
                teamDTOs
        );
    }
}

