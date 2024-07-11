package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;


import org.example.courzelo.dto.requests.TrelloBoardReq;
import org.example.courzelo.models.TicketType;
import org.example.courzelo.models.TrelloBoard;
import org.example.courzelo.repositories.TrelloBoardRepository;
import org.example.courzelo.serviceImpls.ITicketTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/tickettype")
@RestController
@RequiredArgsConstructor
public class TicketTypeController {
    private final ITicketTypeService iTicketTypeService;
    private final TrelloBoardRepository trellorepository;

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
    public Optional<TicketType> getTicketType(@PathVariable String id) {
        return iTicketTypeService.getTicketType(id);
    }

    @DeleteMapping("/delete/{ID}")
    public void delete(@PathVariable String ID) {
        iTicketTypeService.deleteTicketType(ID);
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
    public void update(@RequestBody TicketType ticket) {
        iTicketTypeService.updateTicketType(ticket);
    }

    @GetMapping("/bytype")
    public TrelloBoard findBoard(@RequestParam("type") String type) {
        TicketType tickettype = iTicketTypeService.findByType(type);
        return trellorepository.findByType(tickettype);
    }
}
