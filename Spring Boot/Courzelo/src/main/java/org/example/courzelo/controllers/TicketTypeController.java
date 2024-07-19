package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.TrelloBoardReq;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.models.TicketType;
import org.example.courzelo.models.TrelloBoard;
import org.example.courzelo.repositories.TicketRepository;
import org.example.courzelo.repositories.TrelloBoardRepository;
import org.example.courzelo.serviceImpls.ITicketService;
import org.example.courzelo.serviceImpls.ITicketTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;

@RequestMapping("/api/v1/tickettype")
@RestController
@RequiredArgsConstructor
public class TicketTypeController {
    private final ITicketTypeService iTicketTypeService;
    private final TrelloBoardRepository trellorepository;
    private final ITicketService iTicketService;
    private final TicketRepository ticketRepository;
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @PostMapping("/add")
    public ResponseEntity<TicketType> addTickettype(@RequestBody TicketType ticketType) {
        TicketType savedTicketType = iTicketTypeService.saveTicketType(ticketType);
        return ResponseEntity.ok(savedTicketType); // Return the saved TicketType with its ID
    }

    @GetMapping("/all")
    public List<TicketType> getTicketTypes() {
        return iTicketTypeService.getAllTicketTypes();
    }
    @GetMapping("/get/{id}")
    public TicketType getTicketType(@PathVariable String id) {
        return iTicketTypeService.getTicketType(id);
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> delete(@PathVariable String ID) {
        try {
            iTicketTypeService.deleteTicketType(ID);
            return ResponseEntity.ok(Collections.singletonMap("message", "Ticket deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to delete ticket"));
        }
    }
    @RequestMapping(path="/trello/add",method = RequestMethod.POST)
    public ResponseEntity<?> createBoard(@RequestBody TrelloBoardReq board ){
        TrelloBoard b = new TrelloBoard();
        b.setId(board.getIdBoard());
        b.setIdListToDo(board.getIdListToDo());
        b.setIdListDoing(board.getIdListDoing());
        b.setIdListDone(board.getIdListDone());
        b.setType(iTicketTypeService.findByType(board.getType()));
        return ResponseEntity.ok(trellorepository.save(b));
    }

    @PutMapping("/update/{id}")
    public TicketType update(@RequestBody TicketType ticket) {
        return iTicketTypeService.updateTicketType(ticket);
    }

    @GetMapping("/bytype")
    public TrelloBoard findBoard(@RequestParam("type") String type) {
        TicketType tickettype = iTicketTypeService.findByType(type);
        return trellorepository.findByType(tickettype);
    }

    @DeleteMapping("/delete1/{ID}")
    public ResponseEntity<?> deleteByType(@PathVariable String ID) {
        try {
            // Retrieve the TicketType
            TicketType type = iTicketTypeService.getTicketType(ID);
            logger.info("Received request to get tickettype with ID: {}", type);
            // Get the list of tickets by type
            List<Ticket> tickets = iTicketService.getTicketsByType(type.getId());

            // Delete each ticket
            for (Ticket ticket : tickets) {
                iTicketService.deleteTicket(ticket.getId());
            }

            // Delete the TicketType
            iTicketTypeService.deleteTicketType(ID);

            return ResponseEntity.ok(Collections.singletonMap("message", "Tickets and TicketType deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to delete tickets and TicketType"));
        }
    }

}
