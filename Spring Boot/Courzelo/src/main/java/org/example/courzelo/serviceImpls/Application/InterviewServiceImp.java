package org.example.courzelo.serviceImpls.Application;

import lombok.RequiredArgsConstructor;
import org.example.courzelo.models.Application.Interview;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Application.InterviewRepository;
import org.example.courzelo.services.Application.InterviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImp implements InterviewService {
    private final InterviewRepository repository;


    @Override
    public List<Interview> getInterviewByUser(User user)
    {
        return repository.findByInterviewer(user);
    }

    @Override
    public Interview getInterview(String id) {
        return repository.findById(id).get();
    }



    @Override
    public List<Interview> getAll() {
        return repository.findAll();
    }

    @Override
    public Boolean deleteInterview(String id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Interview saveInterview(Interview interview) {
        return repository.save(interview);
    }

    @Override
    public Boolean updateAdmission(Interview interview) {
        try{
            repository.save(interview);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
