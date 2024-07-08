package org.example.courzelo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.repositories.TicketRepository;

import org.example.courzelo.serviceImpls.ITicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImp implements ITicketService {
    private final TicketRepository ticketRepository;

    @Override
    public Optional<Ticket> getTicket(String id) {
        return ticketRepository.findById(id);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public void deleteTicket(String id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }


    @Override
    public void updateTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }
}
