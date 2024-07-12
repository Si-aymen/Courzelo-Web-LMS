package org.example.courzelo.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.TicketType;
import org.example.courzelo.models.TrelloBoard;
import org.example.courzelo.models.TrelloCard;
import org.example.courzelo.services.TicketTypeServiceImp;
import org.example.courzelo.services.TrelloBoardServiceImp;
import org.example.courzelo.services.TrelloCardServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/Board")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TrelloBoardController {

    private final TrelloBoardServiceImp trelloBoardService;
    private final TicketTypeServiceImp tickettypeservice;
    private final TrelloCardServiceImp trellocardservice;
    @GetMapping
    public List<TrelloBoard> getAllTrelloBoards() {
        return trelloBoardService.findAll();
    }

    @GetMapping("/bytype")
    public TrelloBoard findBoard(@RequestParam("type") String type) {
        TicketType tickettype = tickettypeservice.findByType(type);
        return trelloBoardService.findByType(tickettype);
    }
    @GetMapping("/getCards")
    public List<TrelloCard> findCards() {
        return trellocardservice.findAll();
    }
}
