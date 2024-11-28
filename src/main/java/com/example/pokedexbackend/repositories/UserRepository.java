package com.example.pokedexbackend.repositories;

import com.example.pokedexbackend.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  UserRepository extends MongoRepository<User, ObjectId> {
    boolean existsByUserName(String userName);
    Optional<User> findByUserName(String userName);
}
