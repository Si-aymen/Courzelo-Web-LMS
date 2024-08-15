package org.example.courzelo.services;

import org.example.courzelo.dto.ProfessorDTO;
import org.example.courzelo.exceptions.ResourceNotFoundException;
import org.example.courzelo.models.Professor;
import org.example.courzelo.models.Timeslot;
import org.example.courzelo.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public List<ProfessorDTO> getAllProfessors() {
        return professorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfessorDTO> getProfessorById(String professorId) {
        return professorRepository.findById(professorId).map(this::mapToDTO);
    }

    public ProfessorDTO addProfessor(Professor professor) {
        return mapToDTO(professorRepository.save(professor));
    }

    public ProfessorDTO updateProfessor(Professor professor) {
        return mapToDTO(professorRepository.save(professor));
    }

    public void deleteProfessor(String professorId) {
        professorRepository.deleteById(professorId);
    }

    public Set<Timeslot> getUnavailableTimeSlots(String professorId) {
        Optional<Professor> professor = professorRepository.findById(professorId);
        return professor.map(Professor::getUnavailableTimeSlots).orElse(null);
    }

    public ProfessorDTO updateUnavailableTimeSlots(String professorId, Set<Timeslot> unavailableTimeSlots) {
        Optional<Professor> professorOptional = professorRepository.findById(professorId);
        if (professorOptional.isPresent()) {
            Professor professor = professorOptional.get();
            professor.setUnavailableTimeSlots(unavailableTimeSlots);
            return mapToDTO(professorRepository.save(professor));
        }
        return null;
    }
    private ProfessorDTO mapToDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(professor.getId());
        dto.setName(professor.getName());
        dto.setUnavailableTimeSlots(professor.getUnavailableTimeSlots());
        return dto;
    }
    private Professor mapToEntity(ProfessorDTO dto) {
        Professor professor = new Professor();
        professor.setId(dto.getId());
        professor.setName(dto.getName());
        professor.setUnavailableTimeSlots(dto.getUnavailableTimeSlots());
        return professor;
    }
}
