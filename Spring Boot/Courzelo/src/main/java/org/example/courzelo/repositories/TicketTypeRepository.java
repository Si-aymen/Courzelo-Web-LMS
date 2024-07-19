package org.example.courzelo.repositories;

import org.example.courzelo.models.Ticket;
import org.example.courzelo.models.TicketType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TicketTypeRepository extends MongoRepository <TicketType,String>{
    TicketType findByType(String type);
}








