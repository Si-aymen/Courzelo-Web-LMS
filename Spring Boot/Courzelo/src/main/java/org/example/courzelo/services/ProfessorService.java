package org.example.courzelo.services;

import org.example.courzelo.exceptions.ResourceNotFoundException;
import org.example.courzelo.models.Professor;
import org.example.courzelo.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service

public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public Professor getProfessorById(String id) {
        return professorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
    }

    public Professor updateUnavailableTimeSlots(String id, Set<String> unavailableTimeSlots) {
        Professor professor = professorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
        professor.setUnavailableTimeSlots(unavailableTimeSlots);
        return professorRepository.save(professor);
    }
}
