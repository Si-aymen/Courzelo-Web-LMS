package org.example.courzelo.repositories;

import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserByEmail(String email);
    Optional<User> findByEmail(String s);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
