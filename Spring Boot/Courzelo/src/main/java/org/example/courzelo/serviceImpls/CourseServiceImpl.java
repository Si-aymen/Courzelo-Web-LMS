package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.CoursePostRequest;
import org.example.courzelo.dto.requests.CourseRequest;
import org.example.courzelo.dto.responses.CoursePostResponse;
import org.example.courzelo.dto.responses.CourseResponse;
import org.example.courzelo.models.User;
import org.example.courzelo.models.institution.Course;
import org.example.courzelo.models.institution.CoursePost;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.CourseRepository;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.ICourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class CourseServiceImpl implements ICourseService {
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<HttpStatus> createCourse(String institutionID, CourseRequest courseRequest,Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        User user = userRepository.findUserByEmail(principal.getName());
        Course course = Course.builder()
                .name(courseRequest.getName())
                .description(courseRequest.getDescription())
                .credit(courseRequest.getCredit())
                .institution(institution)
                .teachers(List.of(user))
                .build();
        courseRepository.save(course);
        user.getEducation().getCourses().add(course);
        userRepository.save(user);
        institution.getCourses().add(course);
        institutionRepository.save(institution);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> updateCourse(String courseID, CourseRequest courseRequest) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        course.setCredit(courseRequest.getCredit());
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteCourse(String courseID) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getStudents().forEach(student -> student.getEducation().getCourses().remove(course));
        course.getTeachers().forEach(teacher -> teacher.getEducation().getCourses().remove(course));
        course.getInstitution().getCourses().remove(course);
        courseRepository.delete(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CourseResponse> getCourse(String courseID) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        return ResponseEntity.ok(CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .credit(course.getCredit())
                .teachers(course.getTeachers().stream().map(User::getEmail).toList())
                .students(course.getStudents().stream().map(User::getEmail).toList())
                .posts(course.getPosts().stream().map(coursePost -> CoursePostResponse.builder()
                        .id(coursePost.getId())
                        .title(coursePost.getTitle())
                        .author(coursePost.getAuthor())
                        .created(coursePost.getCreated())
                        .files(getBytesFromFiles(coursePost.getFiles()))
                        .build()).toList())
                .build());
    }

    @Override
    public ResponseEntity<HttpStatus> addTeacher(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        User teacher = course.getInstitution().getTeachers().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElseThrow();
        course.getTeachers().add(teacher);
        teacher.getEducation().getCourses().add(course);
        userRepository.save(teacher);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> removeTeacher(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getTeachers().removeIf(user -> user.getEmail().equals(email));
        User teacher = userRepository.findUserByEmail(email);
        teacher.getEducation().getCourses().removeIf(course1 -> course1.getId().equals(courseID));
        userRepository.save(teacher);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> addStudent(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        User student = course.getInstitution().getStudents().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElseThrow();
        course.getStudents().add(student);
        student.getEducation().getCourses().add(course);
        userRepository.save(student);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> removeStudent(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getStudents().removeIf(user -> user.getEmail().equals(email));
        User student = userRepository.findUserByEmail(email);
        student.getEducation().getCourses().removeIf(course1 -> course1.getId().equals(courseID));
        userRepository.save(student);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> leaveCourse(String courseID, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getStudents().remove(user);
        course.getTeachers().remove(user);
        user.getEducation().getCourses().remove(course);
        userRepository.save(user);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<HttpStatus> addPost(String courseID, CoursePostRequest coursePostRequest) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getPosts().add(CoursePost.builder()
                .title(coursePostRequest.getTitle())
                .author(coursePostRequest.getAuthor())
                .created(coursePostRequest.getCreated())
                .files(uploadFiles(coursePostRequest.getFiles(),coursePostRequest,course.getInstitution()))
                .build());
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deletePost(String courseID, String postID) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        //delete files
        course.getPosts().stream().filter(coursePost -> coursePost.getId().equals(postID)).findFirst().ifPresent(coursePost -> {
            coursePost.getFiles().forEach(file -> {
                try {
                    Files.delete(new File(file).toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        course.getPosts().removeIf(coursePost -> coursePost.getId().equals(postID));
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    private List<byte[]> getBytesFromFiles(List<String> files) {
        return files.stream().map(file -> {
            try {
                return Files.readAllBytes(new File(file).toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
    private List<String> uploadFiles(MultipartFile[] files,CoursePostRequest coursePostRequest,Institution institution) {
        String baseDir = "upload" + File.separator + institution.getName() + File.separator + coursePostRequest.getTitle() + "files" + File.separator;
        return Stream.of(files).map(file -> {
            try {
                File dir = new File(baseDir);
                if (!dir.exists()) {
                    boolean dirsCreated = dir.mkdirs();
                    if (!dirsCreated) {
                        throw new IOException("Failed to create directories");
                    }
                }
                String originalFileName = file.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                // Generate a random filename
                String newFileName = UUID.randomUUID() + extension;
                // Define the path to the new file
                String filePath = baseDir + newFileName;
                log.info("File path: " + filePath);
                Files.copy(file.getInputStream(), new File(filePath).toPath());
                return filePath;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
