package com.example.pokedexbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
    private String id;
    private String teamName;
    private List<String> pokemonNames;
    private List<String> pokemonSprites;
}
