package org.example.courzelo.repositories;

import org.example.courzelo.models.Ticket;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface TicketRepository extends MongoRepository<Ticket,String> {
    List<Ticket> findByUser(User user);
    List<Ticket> findByTypeId(String typeId);


}
