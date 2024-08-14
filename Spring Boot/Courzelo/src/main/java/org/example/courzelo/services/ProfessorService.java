package org.example.courzelo.services;

import org.example.courzelo.exceptions.ResourceNotFoundException;
import org.example.courzelo.models.Professor;
import org.example.courzelo.models.Timeslot;
import org.example.courzelo.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service

public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Optional<Professor> getProfessorById(String professorId) {
        return professorRepository.findById(professorId);
    }

    public Professor addProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor updateProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public void deleteProfessor(String professorId) {
        professorRepository.deleteById(professorId);
    }

    public Set<Timeslot> getUnavailableTimeSlots(String professorId) {
        Optional<Professor> professor = professorRepository.findById(professorId);
        return professor.map(Professor::getUnavailableTimeSlots).orElse(null);
    }

    public Professor updateUnavailableTimeSlots(String professorId, Set<Timeslot> unavailableTimeSlots) {
        Optional<Professor> professorOptional = professorRepository.findById(professorId);
        if (professorOptional.isPresent()) {
            Professor professor = professorOptional.get();
            professor.setUnavailableTimeSlots(unavailableTimeSlots);
            return professorRepository.save(professor);
        }
        return null;
    }
}
