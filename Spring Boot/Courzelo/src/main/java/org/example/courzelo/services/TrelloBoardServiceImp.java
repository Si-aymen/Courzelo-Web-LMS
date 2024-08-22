package org.example.courzelo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.courzelo.models.TicketType;
import org.example.courzelo.models.TrelloBoard;
import org.example.courzelo.repositories.TicketTypeRepository;
import org.example.courzelo.repositories.TrelloBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrelloBoardServiceImp {
    private TrelloBoardRepository trelloBoardRepository;
    private final TicketTypeRepository ticketTypeService;

    @Autowired
    public void TrelloBoardService(TrelloBoardRepository trelloBoardRepository) {
        this.trelloBoardRepository = trelloBoardRepository;
    }

    public ResponseEntity<?> createTrelloBoard(@RequestParam(required = false) String typeId) {
        TicketType ticketType = null;

        if (typeId != null) {
            Optional<TicketType> reclamationTypeOpt = ticketTypeService.findById(typeId);
            if (reclamationTypeOpt.isPresent()) {
                ticketType = reclamationTypeOpt.get();
            } else {
                return ResponseEntity.notFound().build();
            }
        }


        return ResponseEntity.ok("Trello board created successfully");
    }

    public List<TrelloBoard> findAll() {
        return trelloBoardRepository.findAll();
    }

    public Optional<TrelloBoard> findById(String id) {
        return trelloBoardRepository.findById(id);
    }

    public TrelloBoard save(TrelloBoard trelloBoard) {
        return trelloBoardRepository.save(trelloBoard);
    }

    public void deleteById(String id) {
        trelloBoardRepository.deleteById(id);
    }

    public TrelloBoard findByType(TicketType type) {
        return trelloBoardRepository.findByType(type);
    }
}

