package org.example.courzelo.serviceImpls;

import org.example.courzelo.models.Ticket;

import java.util.List;
import java.util.Optional;

public interface ITicketService {
    Optional<Ticket> getTicket(String id);
    List<Ticket> getAllTickets();

    void deleteTicket(String id);


    void saveTicket(Ticket ticket);

    void updateTicket(Ticket ticket);
}
