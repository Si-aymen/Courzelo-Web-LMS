package org.example.courzelo.repositories;

import org.example.courzelo.models.Transports;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TransportsRepository extends MongoRepository<Transports, String> {
}
