package org.example.courzelo.services;

import lombok.AllArgsConstructor;
import org.example.courzelo.models.Activity;
import org.example.courzelo.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public List<Activity> getAllActivities() {
        return this.activityRepository.findAll();
    }

    public Activity getActivityById(String id) {
        return activityRepository.findById(id).orElse(null);
    }

    public Activity addActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public void deleteActivity(String id) {
        activityRepository.deleteById(id);
    }
}
