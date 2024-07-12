package org.example.courzelo.controllers.RecruitementController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.courzelo.models.RecruitementEntities.JobOffer;
import org.example.courzelo.repositories.RecruitementRepo.JobOfferRepo;
import org.example.courzelo.services.RecruitementService.IJobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/jobOffer")
@Tag(name = "JobOffer")

public class JobOfferController {
    @Autowired
    JobOfferRepo jobOfferRepo;
    @Autowired
    IJobOfferService jobOfferService;

    //get all job offers
    @GetMapping("/all")
    public ResponseEntity<List<JobOffer>> getAlljobOffer(){
        try{
            List<JobOffer> jobOffers = jobOfferService.getAlljobOffer();
            if(jobOffers.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(jobOffers);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    //get job offer by title
    @GetMapping("/{title}")
    public ResponseEntity<JobOffer> getJobOfferByTitle(@PathVariable("title") String title){
        try{
            JobOffer jobOffer = jobOfferService.getJobOfferByTitle(title);
            if(jobOffer == null){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(jobOffer);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    //get job offer by id
    @GetMapping("/id/{id}")
    public ResponseEntity<JobOffer> getJobOfferById(@PathVariable("id") String id){
        try{
            Optional<JobOffer> jobOffer = jobOfferRepo.findById(id);
            return jobOffer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    // add job offer
    @PostMapping("/add")
    public ResponseEntity<JobOffer> addJobOffer( @RequestBody JobOffer jobOffer){
        try{
            jobOffer.setRecruiter(null);
            jobOffer.setCandidacies(null);
            jobOfferService.addJobOffer(jobOffer);
            return ResponseEntity.ok(jobOffer);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    //delete job offer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobOffer(@PathVariable("id") String id){
        try{
            jobOfferService.deleteJobOffer(id);
            return ResponseEntity.ok("Job Offer deleted");
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    //update job offer
    @PutMapping("/update/{id}")
    public ResponseEntity<JobOffer> updateJobOffer(@PathVariable("id") String id, @RequestBody JobOffer jobOffer) {
        try {
            jobOfferService.updateJobOffer(id, jobOffer);
            return ResponseEntity.ok(jobOffer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
