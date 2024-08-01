package org.example.courzelo.services;

import org.example.courzelo.models.Stages;
import org.example.courzelo.models.Transports;

import java.util.List;

public interface IStagesService {


    public List<Stages> retrieveAllStages();

    public Stages retrieveStage(String StageId);

    public Stages addStage(Stages stages);

    public void removeStage(String stageID);

    public Stages modifyStage(Stages Stage);

    public  Long GetNumberOfStage();
    public void AssignStudentToInternship(String StudentID , String InternshipID );
}
