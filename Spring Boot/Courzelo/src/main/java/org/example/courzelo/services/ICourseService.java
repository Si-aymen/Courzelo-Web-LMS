package org.example.courzelo.services;

import org.example.courzelo.dto.requests.CoursePostRequest;
import org.example.courzelo.dto.requests.CourseRequest;
import org.example.courzelo.dto.responses.CourseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface ICourseService {
    ResponseEntity<HttpStatus> createCourse(String institutionID,CourseRequest courseRequest,Principal principal);
    ResponseEntity<HttpStatus> updateCourse(String courseID,CourseRequest courseRequest);
    ResponseEntity<HttpStatus> deleteCourse(String courseID);
    ResponseEntity<CourseResponse> getCourse(String courseID);
    ResponseEntity<HttpStatus> setTeacher(String courseID, String email);
    ResponseEntity<HttpStatus> addPost(String courseID, CoursePostRequest coursePostRequest, MultipartFile[] files);
    ResponseEntity<HttpStatus> deletePost(String courseID, String postID);

    ResponseEntity<byte[]> downloadFile(String courseID, String fileName);
}
