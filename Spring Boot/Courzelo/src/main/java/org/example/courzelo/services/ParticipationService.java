package org.example.courzelo.services;

import org.example.courzelo.dto.ParticipationDTO;
import org.example.courzelo.models.Participation;
import org.example.courzelo.repositories.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipationService {
    private final ParticipationRepository participationRepository;

    public ParticipationService(ParticipationRepository participationRepository) {
        this.participationRepository = participationRepository;
    }

    public List<ParticipationDTO> findByStudentId(String studentId) {
        return participationRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ParticipationDTO saveParticipation(ParticipationDTO participationDTO) {
        Participation participation = convertToEntity(participationDTO);
        participation = participationRepository.save(participation);
        return convertToDTO(participation);
    }

    public int getParticipationScore(String studentId) {
        List<Participation> participations = participationRepository.findByStudentId(studentId);
        return participations.stream().mapToInt(Participation::getScore).sum();
    }

    private ParticipationDTO convertToDTO(Participation participation) {
        ParticipationDTO dto = new ParticipationDTO();
        dto.setId(participation.getId());
        dto.setStudentId(participation.getStudentId());
        dto.setScore(participation.getScore());
        return dto;
    }

    private Participation convertToEntity(ParticipationDTO dto) {
        Participation participation = new Participation();
        participation.setId(dto.getId());
        participation.setStudentId(dto.getStudentId());
        participation.setScore(dto.getScore());
        return participation;
    }
}
