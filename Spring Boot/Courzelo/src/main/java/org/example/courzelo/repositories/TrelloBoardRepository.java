package org.example.courzelo.repositories;

import org.example.courzelo.models.TicketType;
import org.example.courzelo.models.TrelloBoard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrelloBoardRepository extends MongoRepository<TrelloBoard,String> {
    TrelloBoard findByType(TicketType type);
}
