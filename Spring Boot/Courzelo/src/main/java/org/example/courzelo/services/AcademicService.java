package org.example.courzelo.services;

import org.example.courzelo.dto.AcademicDTO;
import org.example.courzelo.models.Academic;
import org.example.courzelo.repositories.AcademicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcademicService {
    private final AcademicRepository academicRepository;

    public AcademicService(AcademicRepository academicRepository) {
        this.academicRepository = academicRepository;
    }

    public List<AcademicDTO> findByStudentId(String studentId) {
        return academicRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AcademicDTO saveAcademic(AcademicDTO academicDTO) {
        Academic academic = convertToEntity(academicDTO);
        academic = academicRepository.save(academic);
        return convertToDTO(academic);
    }

    public double getAcademicScore(String studentId) {
        List<Academic> academics = academicRepository.findByStudentId(studentId);
        return academics.stream().mapToDouble(Academic::getScore).average().orElse(0.0);
    }

    private AcademicDTO convertToDTO(Academic academic) {
        AcademicDTO dto = new AcademicDTO();
        dto.setId(academic.getId());
        dto.setStudentId(academic.getStudentId());
        dto.setScore(academic.getScore());
        return dto;
    }

    private Academic convertToEntity(AcademicDTO dto) {
        Academic academic = new Academic();
        academic.setId(dto.getId());
        academic.setStudentId(dto.getStudentId());
        academic.setScore(dto.getScore());
        return academic;
    }
}
