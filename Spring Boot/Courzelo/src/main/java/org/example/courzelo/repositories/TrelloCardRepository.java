package org.example.courzelo.repositories;

import org.example.courzelo.models.TrelloCard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrelloCardRepository extends MongoRepository <TrelloCard,String>{
}
