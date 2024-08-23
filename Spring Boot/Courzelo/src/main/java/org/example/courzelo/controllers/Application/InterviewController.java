package org.example.courzelo.controllers.Application;

import lombok.RequiredArgsConstructor;
import org.example.courzelo.dto.requests.InterviewDTO;
import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.Application.Interview;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Application.AdmissionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.Application.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService service;
    private final UserRepository userRepository;
    private final AdmissionRepository admissionRepository;

    @GetMapping("/all")
    public List<Interview> getAll(){
        return service.getAll();
    }

    @GetMapping("/user/{email}")
    public List<Interview> getByUser(@PathVariable String email){
        User user = userRepository.findUserByEmail(email);
        return service.getInterviewByUser(user);
    }


    @GetMapping("/{id}")
    public Interview getInterview(@PathVariable String id){
        return service.getInterview(id);
    }

    @GetMapping("/interview/{id}")
    public Interview addMembers(@PathVariable String id, @RequestParam List<String> newMembers) {
        Interview interview = service.getInterview(id);

        List<String> interviewees = interview.getInterviewee();

        if (interviewees == null) {
            interviewees = new ArrayList<>();
        }

        interviewees.addAll(newMembers);

        interview.setInterviewee(interviewees);

        Interview updatedInterview = service.saveInterview(interview);

        return updatedInterview;
    }

    @GetMapping("/remove/{id}/")
    public Interview removeMembers(@PathVariable String id, @RequestParam List<String> membersToRemove) {
        Interview interview = service.getInterview(id);

        List<String> interviewees = interview.getInterviewee();

        if (interviewees != null) {
            interviewees.removeAll(membersToRemove);
        }

        interview.setInterviewee(interviewees);

        // Save the updated interview back to the database
        Interview updatedInterview = service.saveInterview(interview);

        // Return the updated interview
        return updatedInterview;
    }


    @PostMapping("/add")
    public ResponseEntity<?> createInterview(@RequestBody InterviewDTO interview) {
        try {
            // Retrieve and log email
            String email = interview.getInterviewer();
            System.out.println("Original USER FRONT DETAILS: '" + email + "'");

            // Remove any leading or trailing spaces
            email = email != null ? email.trim() : "";
            System.out.println("Trimmed USER FRONT DETAILS: '" + email + "'");

            // Retrieve interviewee and admission details
            System.out.println("Le INTERVIEWEES FRONT DETAILS: " + interview.getInterviewee());
            System.out.println("Le ADMISSION FRONT DETAILS: " + interview.getAdmission());

            // Retrieve admission from repository
            Admission admis = admissionRepository.findById(interview.getAdmission())
                    .orElseThrow(() -> new Exception("Admission not found"));

            // Log email for debugging
            System.out.println("Attempting to find user by email: '" + email + "'");

            // Retrieve user from repository
            User user = userRepository.findUserByEmail(email); // Using dynamic email
            if (user == null) {
                System.out.println("Le USER BACK DETAILS: User not found for email: '" + email + "'");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Create and log interview
            Interview interview1 = new Interview();
            interview1.setAdmission(admis);
            interview1.setInterviewee(interview.getInterviewee());
            interview1.setInterviewer(user);
            System.out.println("Le INTERVIEW DETAILS: " + interview1);

            // Save interview and return response
            return ResponseEntity.ok(service.saveInterview(interview1));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }




    @PutMapping
    public ResponseEntity<?> updateInterview(@RequestBody InterviewDTO interv){
        try{
            Interview interview = service.getInterview(interv.getId());
            User user = userRepository.findUserByEmail(interv.getInterviewer());
            interview.setInterviewer(user);
            interview.setInterviewee(interv.getInterviewee());
            //admis.setUniversity(admission.getUniversityId());
            return ResponseEntity.ok(service.saveInterview(interview));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("interview not found");
        }
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteTicket(@PathVariable String ID) {
        try {
            service.deleteInterview(ID);
            return ResponseEntity.ok(Collections.singletonMap("message", "intervie deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to delete Admission"));
        }
    }
}
