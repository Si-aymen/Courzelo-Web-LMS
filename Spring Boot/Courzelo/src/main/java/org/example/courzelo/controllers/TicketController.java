package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.serviceImpls.ITicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/ticket")
@RestController
@RequiredArgsConstructor
public class TicketController {
    private final ITicketService iTicketService;
    @PostMapping("/add")
    public void addTicket(@RequestBody Ticket ticket) {
        iTicketService.saveTicket(ticket);
    }
    @GetMapping("/all")
    public List<Ticket> getReclamations() {
        return iTicketService.getAllTickets();
    }
    @GetMapping("/get/{id}")
    public Optional<Ticket> getReclamation(@PathVariable String id) {
        return iTicketService.getTicket( id);
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
