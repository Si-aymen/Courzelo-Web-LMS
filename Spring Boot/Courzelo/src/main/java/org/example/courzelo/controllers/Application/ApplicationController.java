package org.example.courzelo.controllers.Application;



import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.example.courzelo.dto.requests.ApplicationREQ;
import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.Application.Application;
import org.example.courzelo.models.Application.Status;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Application.AdmissionRepository;
import org.example.courzelo.repositories.Application.ApplicationRepository;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.Application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private final ApplicationService service;
    private final ApplicationRepository repository;
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final AdmissionRepository admissionRepository;


    @GetMapping("/all")
    public List<Application> getAllApplications() {
        return service.getAllApplications();
    }

    @GetMapping("/teachers/{id}")
    public List<String> getTeachers(@PathVariable String id) {
        Admission admission = admissionRepository.findById(id).get();
        List<String> teacher = admission.getInstitution().getTeachers();
        return teacher;
    }
    @GetMapping("/students/{id}")
    public List<String> getStudents(@PathVariable String id) {
        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found with id " + id));
        List<Application> applications = service.getApplicationByAdmission(admission);
        List<String> students = applications.stream()
                .map(Application::getUser)
                .filter(Objects::nonNull)
                .map(User::getEmail)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        return students;
    }
    @GetMapping("/{email}")
    public List<Application> getAllApplicationsByUser(@PathVariable String email) {
        User user = userRepository.findUserByEmail(email);
        return service.getApplicationByUser(user);
    }

    @GetMapping("/admissionstatus/{id}")
    public List<Application> getAllApplicationsByAdmissionAndStatus(@PathVariable String id,@RequestParam Status status) {
        Admission admission = admissionRepository.findById(id).get();
        return service.getApplicationByAdmissionAndStatus(admission,status);
    }
    @GetMapping("/admission/{id}")
    public List<Application> getApplicationByAdmission(@PathVariable String id) {
        Admission admission = admissionRepository.findById(id).get();

        return service.getApplicationByAdmission(admission);
    }
    @PostMapping
    public ResponseEntity<?> createApplication(@RequestBody ApplicationREQ application) {
        try{
        User user = userRepository.findUserByEmail(application.getUserid());
        Admission admission = admissionRepository.findById(application.getAdmissionid()).get();
        Application app = new Application();
        app.setUser(user);
        app.setAdmission(admission);
        return ResponseEntity.ok(service.createApplication(app));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @PutMapping("/{id}")
    public Application updateApplication(@PathVariable String id, @RequestBody Application application) {
        return service.updateApplication(id, application);
    }

    @PostMapping("/note/{id}")
    public Application addNote(@PathVariable String id, @RequestBody Application app) {
        Application application = repository.findById(id).get();
        application.setNote(app.getNote());
        application.setNoted(true);
        return service.updateApplication(id,application);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        service.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{email}/{id}")
    public List<Application> getByAdmissionAndUser(@PathVariable String email, @PathVariable String id){
        Admission admission = admissionRepository.findById(id).get();
        User user = userRepository.findUserByEmail(email);
        return service.getApplicationByAdmissionAndUser(admission,user);
    }
    @PostMapping("/update-statuses")
    public ResponseEntity<String> updateStatuses(
            @RequestParam int acceptedLimit,
            @RequestParam int waitingLimit) {
        service.updateApplicationStatuses(acceptedLimit, waitingLimit);
        return ResponseEntity.ok("Statuses updated successfully.");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(
            @PathVariable("id") String applicationId,
            @RequestParam("status") Status status) {

        Application updatedApplication = service.setStatus(applicationId, status);

        return ResponseEntity.ok(updatedApplication);
    }


}