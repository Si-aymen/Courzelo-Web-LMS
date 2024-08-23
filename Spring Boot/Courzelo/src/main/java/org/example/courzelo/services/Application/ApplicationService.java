package org.example.courzelo.services.Application;


import lombok.AllArgsConstructor;
import org.example.courzelo.configurations.RabbitProducerConfig;
import org.example.courzelo.dto.requests.EmailRequest;
import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.Application.Application;
import org.example.courzelo.models.Application.Status;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Application.AdmissionRepository;
import org.example.courzelo.repositories.Application.ApplicationRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ApplicationService {

    @Autowired
    private ApplicationRepository repository;
    private AdmissionRepository admissionRepository;
    private RabbitTemplate rabbitTemplate;


    public void sendEmail(User user, Application application) {
        String htmlMsg = "<h3>Hello, " + user.getEmail() + "</h3>"
                + "<p>Thank you for applying to our university,:</p>"
                + "<a this is your Result :"+application.getStatus()+"</a>"
                + "<p>If you did not make this request, you can ignore this email.</p>"
                + "<p>Best,</p>"
                + "<p>Courzelo</p>";
        EmailRequest emailRequest = EmailRequest.builder()
                .to(user.getEmail())
                .subject("Admission Verification")
                .text(htmlMsg)
                .build();
        rabbitTemplate.convertAndSend(RabbitProducerConfig.EXCHANGE, RabbitProducerConfig.ROUTING_KEY, emailRequest);
    }
    public List<Application> getAllApplications() {
        return repository.findAll();
    }
    public List<Application> getApplicationByUser(User user){
        return repository.findByUser(user);
    }

    public List<Application> getApplicationByAdmissionAndUser(Admission admission ,User user){
        return repository.findByAdmissionAndUser(admission ,user);
    }

    public List<Application> getApplicationByAdmissionAndStatus(Admission admission , Status status){

        return repository.findByAdmissionAndStatus(admission,status);
    }


    public List<Application> getApplicationByAdmission(Admission admission){
        return repository.findByAdmission(admission);
    }
    public Application createApplication(Application application) {
        return repository.save(application);
    }

    public Application updateApplication(String id, Application application) {
        application.setId(id);
        return repository.save(application);
    }

    public void deleteApplication(String id) {
        repository.deleteById(id);
    }

    public Application setStatus(String applicationId, Status status) {
        // Fetch the application by ID
        Application application = repository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Update the status
        application.setStatus(status);

        // Save the updated application
        return repository.save(application);
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkNotes() {
        // Fetch all admissions
        List<Admission> admissions = admissionRepository.findAll();

        for (Admission admission : admissions) {
            // Fetch all applications for the current admission
            List<Application> applications = repository.findByAdmission(admission);

            // Check if all applications have a note
            boolean allNotesPresent = applications.stream().allMatch(application -> application.getNote() != null);

            if (allNotesPresent) {
                // Sort applications by note in descending order
                applications.sort((a, b) -> Double.compare(b.getNote(), a.getNote()));

                // Assign status based on places and waiting
                int totalPlaces = admission.getPlaces();
                int waitingList = admission.getWaiting();

                for (int i = 0; i < applications.size(); i++) {
                    Application application = applications.get(i);

                    if (i < totalPlaces) {
                        application.setStatus(Status.ACCEPTED);
                    } else if (i < totalPlaces + waitingList) {
                        application.setStatus(Status.WAITING);
                    } else {
                        application.setStatus(Status.NOT_ACCEPTED);
                    }
                    // Save the updated application status
                    repository.save(application);
                }
            }
        }
    }



    @Transactional
    public void updateApplicationStatuses(int acceptedLimit, int waitingLimit) {
        List<Application> applications = repository.findAll();

        int totalApplications = applications.size();
        int acceptedCount = Math.min(acceptedLimit, totalApplications);
        int waitingCount = Math.min(waitingLimit, Math.max(0, totalApplications - acceptedLimit));

        for (int i = 0; i < acceptedCount; i++) {
            applications.get(i).setStatus(Status.ACCEPTED);
        }

        for (int i = acceptedCount; i < acceptedCount + waitingCount; i++) {
            applications.get(i).setStatus(Status.WAITING);
        }

        for (int i = acceptedCount + waitingCount; i < totalApplications; i++) {
            applications.get(i).setStatus(Status.NOT_ACCEPTED);
        }

        repository.saveAll(applications);
    }
}
