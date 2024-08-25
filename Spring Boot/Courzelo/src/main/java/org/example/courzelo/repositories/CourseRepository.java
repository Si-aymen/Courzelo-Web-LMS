package org.example.courzelo.repositories;

import org.example.courzelo.models.institution.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends MongoRepository<Course,String> {
}
