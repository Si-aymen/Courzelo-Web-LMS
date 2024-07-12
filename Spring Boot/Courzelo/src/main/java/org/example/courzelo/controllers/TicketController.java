package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.TicketREQ;
import org.example.courzelo.dto.requests.TrelloCardReq;
import org.example.courzelo.models.Status;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.models.TicketType;
import org.example.courzelo.models.TrelloCard;
import org.example.courzelo.repositories.TicketRepository;
import org.example.courzelo.repositories.TrelloCardRepository;
import org.example.courzelo.serviceImpls.ITicketService;
import org.example.courzelo.serviceImpls.ITicketTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.velocity.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/v1/ticket")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketController {
    private final ITicketService iTicketService;
    private final ITicketTypeService typeService;
    private final TrelloCardRepository trellorepository;
    private final TicketRepository ticketrepository;

    @GetMapping("/all")
    public List<Ticket> getReclamations() {
        return iTicketService.getAllTickets();
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTicket(@RequestBody TicketREQ ticket) {
        try {
            TicketType type = typeService.findByType(ticket.getType());
            Ticket tick = new Ticket();
            tick.setSujet(ticket.getSujet());
            tick.setDetails(ticket.getDetails());
            tick.setStatus(Status.EN_ATTENTE);
            tick.setType(type);
            iTicketService.saveTicket(tick);
            return ResponseEntity.ok().body("{\"message\": \"Ticket ajouté avec succès!\"}"); // Return JSON object
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Échec de l'ajout du ticket: " + e.getMessage() + "\"}"); // Return JSON object
        }
    }
    @GetMapping("/get/{id}")
    public Optional<Ticket> getReclamation(@PathVariable String id) {
        return iTicketService.getTicket( id);
    }

    @RequestMapping(path = "/card/add", method = RequestMethod.POST)
    public ResponseEntity<?> createCard(@RequestBody TrelloCardReq cardReq) {
        // Validate the request body fields
        if (cardReq.getCardId() == null || cardReq.getTicketID() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CardId and TicketID must not be null");
        }

        TrelloCard card = new TrelloCard();
        card.setIdCard(cardReq.getCardId());

        Optional<Ticket> ticketOptional = iTicketService.getTicket(cardReq.getTicketID());
        if (ticketOptional.isPresent()) {
            card.setTicket(ticketOptional.get());
            return ResponseEntity.ok(trellorepository.save(card));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found");
        }
    }

    @PostMapping("/update/statusdoing/{idticket}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable("idticket") String id, @PathVariable("status") String status) {

            Ticket t = iTicketService.getTicket(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket not exist with id :" + id));

            if (status.equals(Status.EN_COURS.name()))
                t.setStatus(Status.EN_COURS);

            return ResponseEntity.ok(ticketrepository.save(t));
    }

    @PostMapping("/update/statusdone/{idticket}/{status}")
    public ResponseEntity<?> updateStatusDone(@PathVariable("idticket") String id, @PathVariable("status") String status) {

        Ticket t = iTicketService.getTicket(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not exist with id :" + id));
        if (Status.FINIE.name().equals(status) && t.getStatus() != Status.FINIE)
            t.setStatus(Status.FINIE);
        return ResponseEntity.ok(ticketrepository.save(t));
    }



    @DeleteMapping("/delete/{ID}")
    public void deleteClass(@PathVariable String ID) {
        iTicketService.deleteTicket(ID);
    }

    @PutMapping("/update/{id}")
    public void updateClass(@RequestBody Ticket ticket) {
        iTicketService.updateTicket(ticket);
    }
}
