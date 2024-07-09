package org.example.courzelo.controllers;

import lombok.AllArgsConstructor;
import org.example.courzelo.models.Activity;
import org.example.courzelo.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200") // Replace with your Angular app URL
@RestController
@AllArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/retrieve-activities")
    public List<Activity> getAllActivities()
    {
        return activityService.getAllActivities();
    }

    @GetMapping("/{id}")
    public Activity getActivityById(@PathVariable String id) {
        return activityService.getActivityById(id);
    }

    @PostMapping
    public Activity addActivity(@RequestBody Activity activity) {
        return activityService.addActivity(activity);
    }

    @PutMapping("/{id}")
    public Activity updateActivity(@PathVariable String id, @RequestBody Activity activity) {
        activity.setId(id);
        return activityService.updateActivity(activity);
    }

    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable String id) {
        activityService.deleteActivity(id);
    }





}
