package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;


import org.example.courzelo.models.TicketType;
import org.example.courzelo.serviceImpls.ITicketTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/tickettype")
@RestController
@RequiredArgsConstructor
public class TicketTypeController {
    private final ITicketTypeService iTicketTypeService;
    @PostMapping("/add")
    public void addTicket(@RequestBody TicketType ticketType) {
        iTicketTypeService.saveTicketType(ticketType);
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

    @PutMapping("/update/{id}")
    public void update(@RequestBody TicketType ticket) {
        iTicketTypeService.updateTicketType(ticket);
    }
}
