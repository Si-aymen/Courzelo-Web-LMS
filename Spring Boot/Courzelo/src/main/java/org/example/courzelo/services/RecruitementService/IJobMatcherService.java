package org.example.courzelo.services.RecruitementService;

import org.example.courzelo.models.RecruitementEntities.JobOffer;

import java.util.List;

public interface IJobMatcherService {
    //match jobs
    public List<JobOffer> matchJobs(List<String> candidateSkills, List<String> candidateSpecialities, List<JobOffer> availableJobs);
    //calculate matching score
    public int calculateMatchingScore(List<String> candidateSkills, List<String> candidateSpecialities, JobOffer jobOffer);
    // calculate score
    public int calculateScore(List<String> candidateAttributes,List<String> jobAttributes, int weight);

}
