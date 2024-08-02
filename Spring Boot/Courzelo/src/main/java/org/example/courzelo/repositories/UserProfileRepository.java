package org.example.courzelo.repositories;

import org.example.courzelo.models.ProjectEntities.Event;
import org.example.courzelo.models.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<UserProfile, String> {
    UserProfile findByName(String name);
}
