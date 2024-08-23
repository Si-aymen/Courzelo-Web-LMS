package org.example.courzelo.serviceImpls.Application;

import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MyService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void myMethod() {
        // Your method logic here
        System.out.println("Method executed.");

        // Schedule a task to run after the method completes
        scheduler.schedule(this::scheduledTask, 5, TimeUnit.SECONDS);
    }

    private void scheduledTask() {
        System.out.println("Scheduled task executed.");
    }
}
