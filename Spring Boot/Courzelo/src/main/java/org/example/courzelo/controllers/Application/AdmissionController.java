package org.example.courzelo.controllers.Application;

import lombok.RequiredArgsConstructor;
import org.example.courzelo.dto.AdmissionREQ;
import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.Application.Application;
import org.example.courzelo.models.Application.Status;
import org.example.courzelo.models.User;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.Application.AdmissionRepository;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.Application.AdmissionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admission")
public class AdmissionController {
    private final AdmissionServiceImpl service;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;


    @GetMapping("/all")
    public List<Admission> getAll(){
        return service.getAll();
    }

    @GetMapping("/user/{email}")
    public List<Admission> getByUser(@PathVariable String email){
        User user = userRepository.findUserByEmail(email);
        return service.getAdmissionByUser(user);
    }

    @PutMapping("/{id}/places")
    public ResponseEntity<Admission> updatePlaces(
            @PathVariable("id") String admissionId,
            @RequestParam("places") int places,
            @RequestParam("waiting") int waiting) {
        Admission updatedAdmission = service.getAdmission(admissionId);
        updatedAdmission.setPlaces(places);
        updatedAdmission.setWaiting(waiting);
        return ResponseEntity.ok(service.saveAdmission(updatedAdmission));
    }

    @GetMapping("/{id}")
    public Admission getAdmission(@PathVariable String id){
        return service.getAdmission(id);
    }


    @PostMapping("/add")
    public ResponseEntity<?> createAdmission(@RequestBody AdmissionREQ admission){
        try {
            Institution inst = institutionRepository.findById(admission.getUniversityId()).get();
            System.out.println("le inst "+inst);
            Admission admis = new Admission();
            User user = userRepository.findUserByEmail(admission.getUser());
            admis.setUser(user);
            admis.setDescription(admission.getDescription());
            admis.setTitle(admission.getTitle());
            admis.setStarDate(admission.getStartDate());
            admis.setEndDate(admission.getEndDate());
            admis.setInstitution(inst);
            admis.setPlaces(admission.getPlaces());
            admis.setWaiting(admission.getWaiting());
            System.out.println("le places "+admission.getWaiting());
            System.out.println("le inst "+admis.getWaiting());
            System.out.println("Le ADMISSION DETAILS" + admis);
            return ResponseEntity.ok(service.saveAdmission(admis));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }


    @PutMapping
    public ResponseEntity<?> updateAdmission(@RequestBody AdmissionREQ admission){
        try{
            Admission admis = service.getAdmission(admission.getId());
            admis.setDescription(admission.getDescription());
            admis.setTitle(admission.getTitle());
            admis.setStarDate(admission.getStartDate());
            admis.setEndDate(admission.getEndDate());
            //admis.setUniversity(admission.getUniversityId());
            return ResponseEntity.ok(service.saveAdmission(admis));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admission not found");
        }
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteTicket(@PathVariable String ID) {
        try {
            service.deleteAdmission(ID);
            return ResponseEntity.ok(Collections.singletonMap("message", "Admission deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to delete Admission"));
        }
    }
}
