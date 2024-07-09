package org.example.courzelo.repositories;

import org.example.courzelo.models.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends MongoRepository<Activity,String> {
}
