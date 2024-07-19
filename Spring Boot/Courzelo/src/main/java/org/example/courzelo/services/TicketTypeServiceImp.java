package org.example.courzelo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.TicketType;
import org.example.courzelo.repositories.TicketRepository;
import org.example.courzelo.repositories.TicketTypeRepository;
import org.example.courzelo.serviceImpls.ITicketTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketTypeServiceImp implements ITicketTypeService {
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    @Override
    public TicketType getTicketType(String id) {
        return ticketTypeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("TicketType not found with id: " + id));
    }

    @Override
    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    @Override
    public void deleteTicketType(String id) {
        ticketTypeRepository.deleteById(id);
    }

    @Override
    public TicketType saveTicketType(TicketType type) {
         ticketTypeRepository.save(type);
        return type;
    }

    @Override
    public TicketType findByType(String type) {
        return ticketTypeRepository.findByType(type);
    }


    @Override
    public TicketType updateTicketType(TicketType type) {
        return ticketTypeRepository.save(type);
    }

}
