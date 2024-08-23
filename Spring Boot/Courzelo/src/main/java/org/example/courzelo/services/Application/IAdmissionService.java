package org.example.courzelo.services.Application;

import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.User;

import java.util.List;
import java.util.Optional;

public interface IAdmissionService {
    List<Admission> getAdmissionByUser(User user);
    Admission getAdmission(String id);
    List<Admission> getAll();

    Boolean deleteAdmission(String id);

    Admission saveAdmission(Admission admission);

    Boolean updateAdmission(Admission admission);
}
