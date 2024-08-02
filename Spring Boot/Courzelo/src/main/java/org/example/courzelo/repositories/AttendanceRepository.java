package org.example.courzelo.repositories;

import org.example.courzelo.models.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    List<Attendance> findByStudentIdAndDate(String studentId, LocalDate date);
    List<Attendance> findByDate(LocalDate date);


    List<Attendance> findByStudentId(String studentId);

    List<Attendance> findByStudentNameAndDateBetween(String studentName, LocalDate startDate, LocalDate endDate);
  /*  @Query("{ 'studentId': ?0, 'date': { $gte: ?1, $lte: ?2 } }")
    List<Attendance> findByStudentIdAndDateRange(String studentId, LocalDate startDate, LocalDate endDate);*/
}
