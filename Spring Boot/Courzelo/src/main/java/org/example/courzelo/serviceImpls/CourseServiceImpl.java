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
import java.util.NoSuchElementException;
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
                .institutionID(institution.getId())
                .teachers(List.of(user.getEmail()))
                .build();
        courseRepository.save(course);
        user.getEducation().getCoursesID().add(course.getId());
        userRepository.save(user);
        institution.getCoursesID().add(course.getId());
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
        if(course.getStudents()!= null) {
            course.getStudents().forEach(student -> {
                User user = userRepository.findUserByEmail(student);
                user.getEducation().getCoursesID().remove(course.getId());
                userRepository.save(user);
            });
        }
        if(course.getTeachers()!= null) {
            course.getTeachers().forEach(teacher -> {
                User user = userRepository.findUserByEmail(teacher);
                user.getEducation().getCoursesID().remove(course.getId());
                userRepository.save(user);
            });
        }
        Institution institution = institutionRepository.findById(course.getInstitutionID()).orElseThrow();
        institution.getCoursesID().remove(course.getId());
        institutionRepository.save(institution);
        courseRepository.delete(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CourseResponse> getCourse(String courseID) {
        Course course = courseRepository.findById(courseID).orElseThrow(() -> new NoSuchElementException("Course not found"));
        return ResponseEntity.ok(CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .credit(course.getCredit())
                .teachers(course.getTeachers())
                .students(course.getStudents())
                .posts(course.getPosts() != null ? course.getPosts().stream().map(coursePost -> CoursePostResponse.builder()
                        .id(coursePost.getId())
                        .title(coursePost.getTitle())
                        .author(coursePost.getAuthor())
                        .created(coursePost.getCreated())
                        .files(getBytesFromFiles(coursePost.getFiles()))
                        .build()).toList() : List.of())
                .build());
    }

    @Override
    public ResponseEntity<HttpStatus> addTeacher(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        Institution institution = institutionRepository.findById(course.getInstitutionID()).orElseThrow();
        if(institution.getTeachers().contains(email)){
            User teacher = userRepository.findUserByEmail(email);
            course.getTeachers().add(teacher.getEmail());
            teacher.getEducation().getCoursesID().add(course.getId());
            userRepository.save(teacher);
            courseRepository.save(course);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<HttpStatus> removeTeacher(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getTeachers().removeIf(user -> user.equals(email));
        User teacher = userRepository.findUserByEmail(email);
        teacher.getEducation().getCoursesID().removeIf(course1 -> course1.equals(courseID));
        userRepository.save(teacher);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> addStudent(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        Institution institution = institutionRepository.findById(course.getInstitutionID()).orElseThrow();
        if(institution.getStudents().contains(email)){
            User student = userRepository.findUserByEmail(email);
            course.getStudents().add(student.getEmail());
            student.getEducation().getCoursesID().add(course.getId());
            userRepository.save(student);
            courseRepository.save(course);
            return ResponseEntity.ok(HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<HttpStatus> removeStudent(String courseID, String email) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getStudents().removeIf(user -> user.equals(email));
        User student = userRepository.findUserByEmail(email);
        student.getEducation().getCoursesID().removeIf(course1 -> course1.equals(courseID));
        userRepository.save(student);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> leaveCourse(String courseID, Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        Course course = courseRepository.findById(courseID).orElseThrow();
        course.getStudents().remove(user.getEmail());
        course.getTeachers().remove(user.getEmail());
        user.getEducation().getCoursesID().remove(course.getId());
        userRepository.save(user);
        courseRepository.save(course);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @Override
    public ResponseEntity<HttpStatus> addPost(String courseID, CoursePostRequest coursePostRequest) {
        Course course = courseRepository.findById(courseID).orElseThrow();
        Institution institution = institutionRepository.findById(course.getInstitutionID()).orElseThrow();
        course.getPosts().add(CoursePost.builder()
                .title(coursePostRequest.getTitle())
                .author(coursePostRequest.getAuthor())
                .created(coursePostRequest.getCreated())
                .files(uploadFiles(coursePostRequest.getFiles(),coursePostRequest,institution))
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
