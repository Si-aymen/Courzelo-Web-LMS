package org.example.courzelo.repositories;

import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
}
