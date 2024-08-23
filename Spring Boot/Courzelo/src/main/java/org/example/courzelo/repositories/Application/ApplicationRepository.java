package org.example.courzelo.repositories.Application;


import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.Application.Application;
import org.example.courzelo.models.Application.Status;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByUser(User user);
    List<Application> findByAdmission(Admission admission);
    List<Application> findByAdmissionAndStatus(org.example.courzelo.models.Application.Admission admission, Status status);

    List<Application> findByAdmissionAndUser(Admission admission, User user);

}

