package org.example.courzelo.serviceImpls.Application;

import lombok.RequiredArgsConstructor;
import org.example.courzelo.models.Application.Admission;
import org.example.courzelo.models.Application.AdmissionStatus;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Application.AdmissionRepository;
import org.example.courzelo.services.Application.IAdmissionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdmissionServiceImpl implements IAdmissionService {

    private final AdmissionRepository repository;
    @Override
    public List<Admission> getAdmissionByUser(User user) {
        return repository.findByUser(user);
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void updateAdmissionStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Admission> expiredAdmissions = repository.findByEndDateBeforeAndStatus(now, AdmissionStatus.Open);

        for (Admission admission : expiredAdmissions) {
            admission.setStatus(AdmissionStatus.Closed);
            repository.save(admission);
        }
    }


    @Override
    public Admission getAdmission(String id) {
        return repository.findById(id).get();
    }

    @Override
    public List<Admission> getAll() {
        return repository.findAll();
    }

    @Override
    public Boolean deleteAdmission(String id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Admission saveAdmission(Admission admission) {
        return repository.save(admission);
    }


    @Override
    public Boolean updateAdmission(Admission admission) {
        try{
            repository.save(admission);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
