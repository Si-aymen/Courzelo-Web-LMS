package org.example.courzelo.repositories;

import org.example.courzelo.models.FAQ;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FaqRepository extends MongoRepository<FAQ,String> {
}
