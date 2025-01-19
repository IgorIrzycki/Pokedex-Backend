package com.example.pokedexbackend.repositories;

import com.example.pokedexbackend.models.Team;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends MongoRepository<Team, ObjectId> {
    Optional<Team> findById(ObjectId id);
}
