package org.example.courzelo.serviceImpls;

import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Stages;
import org.example.courzelo.repositories.StagesRepository;
import org.example.courzelo.services.IStagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StagesService implements IStagesService {
    @Autowired
    StagesRepository stagesRepository;
    @Override
    public List<Stages> retrieveAllStages() {
        return stagesRepository.findAll();
    }

    @Override
    public Stages retrieveStage(String StageId) {
        return stagesRepository.findById(StageId).get();
    }

    @Override
    public Stages addStage(Stages s) {
        return stagesRepository.save(s);
    }

    @Override
    public void removeStage(String stageID) {
        stagesRepository.deleteById(stageID);

    }

    @Override
    public Stages modifyStage(Stages Stage) {
        return null;
    }

    @Override
    public Long GetNumberOfStage() {
        return stagesRepository.count() ;
    }
}
