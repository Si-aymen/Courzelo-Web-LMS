package org.example.courzelo.services.Application;

import org.example.courzelo.models.Application.Interview;
import org.example.courzelo.models.User;

import java.util.List;

public interface InterviewService {
    List<Interview> getInterviewByUser(User user);
    Interview getInterview(String id);
    List<Interview> getAll();
    Boolean deleteInterview(String id);
    Interview saveInterview(Interview interview);
    Boolean updateAdmission(Interview interview);
}
