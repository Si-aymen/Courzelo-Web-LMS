package org.example.courzelo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.models.TrelloCard;
import org.example.courzelo.repositories.TicketRepository;
import org.example.courzelo.repositories.TrelloCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrelloCardServiceImp {
    private TrelloCardRepository trelloCardRepository;
    private final TicketRepository ticketService;

    @Autowired
    public void TrelloCardService(TrelloCardRepository trelloCardRepository) {
        this.trelloCardRepository = trelloCardRepository;
    }

    public ResponseEntity<?> createTrellCard(@RequestParam(required = false) String Id) {
        Ticket ticket = null;

        if (Id != null) {
            Optional<Ticket> TicketOpt = ticketService.findById(Id);
            if (TicketOpt.isPresent()) {
                ticket = TicketOpt.get();
            } else {
                return ResponseEntity.notFound().build();
            }
        }


        return ResponseEntity.ok("Trello board created successfully");
    }

    public List<TrelloCard> findAll() {
        return trelloCardRepository.findAll();
    }

    public Optional<TrelloCard> findById(String id) {
        return trelloCardRepository.findById(id);
    }

    public TrelloCard save(TrelloCard trelloBoard) {
        return trelloCardRepository.save(trelloBoard);
    }

    public void deleteById(String id) {
        trelloCardRepository.deleteById(id);
    }

}
