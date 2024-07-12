package org.example.courzelo.services.RecruitementService;

import org.example.courzelo.models.RecruitementEntities.JobOffer;

import java.util.List;

public interface IJobOfferService {
    //get all job offer
    public List<JobOffer> getAlljobOffer();
    //get job offer by title
    public JobOffer getJobOfferByTitle(String title);
    // add job offer
    public void addJobOffer(JobOffer jobOffer);
    //delete job offer
    public void deleteJobOffer(String id);
    //update job offer
    public void updateJobOffer(String id,JobOffer jobOffer);
    //get job offer by id
    public JobOffer getJobOfferById(String id);
}
