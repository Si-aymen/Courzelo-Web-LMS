package org.example.courzelo.repositories;

import org.example.courzelo.models.TicketType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketTypeRepository extends MongoRepository <TicketType,String> {
}
