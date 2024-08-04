package org.example.courzelo.services;

import org.example.courzelo.dto.requests.CoursePostRequest;
import org.example.courzelo.dto.requests.CourseRequest;
import org.example.courzelo.dto.responses.CourseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface ICourseService {
    ResponseEntity<HttpStatus> createCourse(String institutionID,CourseRequest courseRequest,Principal principal);
    ResponseEntity<HttpStatus> updateCourse(String courseID,CourseRequest courseRequest);
    ResponseEntity<HttpStatus> deleteCourse(String courseID);
    ResponseEntity<CourseResponse> getCourse(String courseID);
    ResponseEntity<HttpStatus> addTeacher(String courseID,String email);
    ResponseEntity<HttpStatus> removeTeacher(String courseID,String email);
    ResponseEntity<HttpStatus> addStudent(String courseID,String email);
    ResponseEntity<HttpStatus> removeStudent(String courseID,String email);
    ResponseEntity<HttpStatus> leaveCourse(String courseID, Principal principal);
    ResponseEntity<HttpStatus> addPost(String courseID, CoursePostRequest coursePostRequest);
    ResponseEntity<HttpStatus> deletePost(String courseID, String postID);

}
