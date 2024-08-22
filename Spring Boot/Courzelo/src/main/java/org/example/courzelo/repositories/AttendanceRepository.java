package org.example.courzelo.repositories;

import org.example.courzelo.models.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByStudent(String studentEmail);
    int countByStudentAndPresentTrue(String studentEmail);
}
