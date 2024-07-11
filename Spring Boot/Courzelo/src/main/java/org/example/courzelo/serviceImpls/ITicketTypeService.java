package org.example.courzelo.serviceImpls;

import org.example.courzelo.models.TicketType;

import java.util.List;
import java.util.Optional;

public interface ITicketTypeService {
    Optional<TicketType> getTicketType(String id);
    List<TicketType> getAllTicketTypes();

    void deleteTicketType(String id);


    TicketType saveTicketType(TicketType ticket);

    void updateTicketType(TicketType ticket);

    TicketType findByType(String type);
}
