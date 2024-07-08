package org.example.courzelo.repositories;

import org.example.courzelo.models.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TicketRepository extends MongoRepository<Ticket,String> {

}
