package org.example.courzelo.serviceImpls;

import org.example.courzelo.models.Ticket;
import org.example.courzelo.models.User;

import java.util.List;
import java.util.Optional;

public interface ITicketService {

    List<Ticket> getTicketsByUser(User user);
    Optional<Ticket> getTicket(String id);
    List<Ticket> getAllTickets();

    void deleteTicket(String id);


    void saveTicket(Ticket ticket);
    List<Ticket> getTicketsByType(String typeId);

    void updateTicket(Ticket ticket);
    Ticket updateTicket1(String id, String sujet, String details);
}
