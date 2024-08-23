package org.example.courzelo.repositories.Application;

import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.Application.AdmissionStatus;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdmissionRepository extends MongoRepository<Admission, String> {
    List<Admission> findByUser(User user);
    List<Admission> findByEndDateBeforeAndStatus(LocalDateTime endDate, AdmissionStatus status);

}
