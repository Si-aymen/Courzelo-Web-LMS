package org.example.courzelo.controllers.institution;

import lombok.AllArgsConstructor;
import org.example.courzelo.dto.requests.CoursePostRequest;
import org.example.courzelo.dto.requests.CourseRequest;
import org.example.courzelo.dto.responses.CourseResponse;
import org.example.courzelo.security.CustomAuthorization;
import org.example.courzelo.services.ICourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/course")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
public class CourseController {
    private final ICourseService iCourseService;
    private final CustomAuthorization customAuthorization;
    @PostMapping("/{institutionID}/add")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canCreateCourse(#institutionID)")
    public ResponseEntity<HttpStatus> addCourse(@PathVariable String institutionID,@RequestBody CourseRequest courseRequest) {
        return iCourseService.createCourse(institutionID,courseRequest);
    }
    @PutMapping("/{courseID}/update")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> updateCourse(@PathVariable String courseID,@RequestBody CourseRequest courseRequest) {
        return iCourseService.updateCourse(courseID,courseRequest);
    }
    @DeleteMapping("/{courseID}/delete")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable String courseID) {
        return iCourseService.deleteCourse(courseID);
    }
    @GetMapping("/{courseID}")
    @PreAuthorize("isAuthenticated()&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable String courseID) {
        return iCourseService.getCourse(courseID);
    }
    @PutMapping("/{courseID}/addTeacher")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> addTeacher(@PathVariable String courseID,@RequestParam String email) {
        return iCourseService.addTeacher(courseID,email);
    }
    @PutMapping("/{courseID}/removeTeacher")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> removeTeacher(@PathVariable String courseID,@RequestParam String email) {
        return iCourseService.removeTeacher(courseID,email);
    }
    @PutMapping("/{courseID}/addStudent")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> addStudent(@PathVariable String courseID,@RequestParam String email) {
        return iCourseService.addStudent(courseID,email);
    }
    @PutMapping("/{courseID}/removeStudent")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> removeStudent(@PathVariable String courseID,@RequestParam String email) {
        return iCourseService.removeStudent(courseID,email);
    }
    @PutMapping("/{courseID}/leave")
    @PreAuthorize("isAuthenticated()&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> leaveCourse(@PathVariable String courseID, Principal principal) {
        return iCourseService.leaveCourse(courseID,principal);
    }
    @PutMapping("/{courseID}/addPost")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> addPost(@PathVariable String courseID,@RequestBody CoursePostRequest coursePostRequest) {
        return iCourseService.addPost(courseID,coursePostRequest);
    }
    @DeleteMapping("/{courseID}/deletePost")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable String courseID,@RequestParam String postID) {
        return iCourseService.deletePost(courseID,postID);
    }
}
