package org.example.courzelo.serviceImpls;

import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Stages;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.StagesRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IStagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StagesService implements IStagesService {
    @Autowired
    private StagesRepository stagesRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Stages> retrieveAllStages() {
        if (stagesRepository != null) {
            return stagesRepository.findAll();
        } else {
            log.error("stagesRepository is null");
            return List.of();
        }
    }

    @Override
    public Stages retrieveStage(String StageId) {
        if (stagesRepository != null) {
            Optional<Stages> stage = stagesRepository.findById(StageId);
            return stage.orElse(null);
        } else {
            log.error("stagesRepository is null");
            return null;
        }
    }

    @Override
    public Stages addStage(Stages s) {
        if (stagesRepository != null) {
            return stagesRepository.save(s);
        } else {
            log.error("stagesRepository is null");
            return null;
        }
    }

    @Override
    public void removeStage(String stageID) {
        if (stagesRepository != null) {
            stagesRepository.deleteById(stageID);
        } else {
            log.error("stagesRepository is null");
        }
    }

    @Override
    public Stages modifyStage(Stages Stage) {
        if (stagesRepository != null) {
            return stagesRepository.save(Stage);
        } else {
            log.error("stagesRepository is null");
            return null;
        }
    }

    @Override
    public Long GetNumberOfStage() {
        if (stagesRepository != null) {
            return stagesRepository.count();
        } else {
            log.error("stagesRepository is null");
            return 0L;
        }
    }

    @Override
    public void AssignStudentToInternship(String StudentID, String InternshipID) {
        User student = userRepository.findById(StudentID).orElse(null);
        if (student == null) {
            log.error("Student with ID {} not found", StudentID);
            return;
        }
        if (stagesRepository == null) {
            log.error("stagesRepository is null");
            return;
        }
        Stages stage = stagesRepository.findById(InternshipID).orElse(null);
        if (stage == null) {
            log.error("Stages with ID {} not found", InternshipID);
            return;
        }
        stage.getUserSet().add(student);
        stagesRepository.save(stage);
    }

}
