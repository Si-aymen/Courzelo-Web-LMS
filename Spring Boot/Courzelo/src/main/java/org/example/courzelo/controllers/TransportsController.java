package org.example.courzelo.controllers;


import lombok.AllArgsConstructor;
import org.example.courzelo.dto.responses.LoginResponse;
import org.example.courzelo.models.Transports;
import org.example.courzelo.services.ITransportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transports")
@AllArgsConstructor
@PreAuthorize("permitAll()")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")

public class TransportsController {
    ITransportsService transportsService ;

    @GetMapping("/GetAll")
    public List<Transports> getAllTransports() {
        return transportsService.retrieveAllTransports();
    }

    @GetMapping("/count/transports")
    public Long getNumberOfTransports() {
        return transportsService.GetNumberOfTransports();
    }

    @GetMapping("/GetById/transports/{Transports-id}")
    public Transports getBlocs(@PathVariable("Transports-id") String transprotsId) {
        return transportsService.retrieveTransport(transprotsId);
    }


    @PostMapping("/add-Transports")
    public Transports addTransport(@RequestBody Transports transports) {
        return transportsService.addTransports(transports);
    }

    @DeleteMapping("/remove-Transports/{Transports-id}")
    public void removeTransports(@PathVariable("Transports-id") String transprotsId) {
        transportsService.removeTransports(transprotsId);
    }


}
