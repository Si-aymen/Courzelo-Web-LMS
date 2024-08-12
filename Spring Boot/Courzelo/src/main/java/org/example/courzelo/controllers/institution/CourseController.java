package org.example.courzelo.controllers.institution;

import jakarta.validation.constraints.NotNull;
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
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<HttpStatus> addCourse(@PathVariable String institutionID,@RequestBody CourseRequest courseRequest,Principal principal) {
        return iCourseService.createCourse(institutionID,courseRequest,principal);
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
    @PutMapping("/{courseID}/setTeacher")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> setTeacher(@PathVariable String courseID,@RequestParam String email) {
        return iCourseService.setTeacher(courseID,email);
    }
    @PutMapping("/{courseID}/addPost")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> addPost(
            @PathVariable String courseID,
            @RequestPart("title") String title,
            @RequestPart("description") String description,
            @RequestPart("files") MultipartFile[] files) {
        CoursePostRequest coursePostRequest = CoursePostRequest.builder().title(title).description(description).build();
        return iCourseService.addPost(courseID, coursePostRequest, files);
    }
    @PreAuthorize("isAuthenticated()&&@customAuthorization.canAccessCourse(#courseID)")
    @GetMapping("/{courseID}/{fileName:.+}/download")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable @NotNull String courseID,@PathVariable @NotNull String fileName) {
        return iCourseService.downloadFile(courseID,fileName);
    }
    @DeleteMapping("/{courseID}/deletePost")
    @PreAuthorize("hasRole('TEACHER')&&@customAuthorization.canAccessCourse(#courseID)")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable String courseID,@RequestParam String postID) {
        return iCourseService.deletePost(courseID,postID);
    }
}
