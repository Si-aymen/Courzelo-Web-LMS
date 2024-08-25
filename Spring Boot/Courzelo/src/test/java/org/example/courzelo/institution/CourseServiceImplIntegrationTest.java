package org.example.courzelo.institution;
import org.example.courzelo.dto.requests.CourseRequest;
import org.example.courzelo.dto.responses.CourseResponse;
import org.example.courzelo.models.User;
import org.example.courzelo.models.institution.Course;
import org.example.courzelo.models.institution.Group;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.CourseRepository;
import org.example.courzelo.repositories.GroupRepository;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.CourseServiceImpl;
import org.example.courzelo.services.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceImplIntegrationTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private QuizService quizService;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourse_ShouldCreateCourseSuccessfully() {
        // Arrange
        String institutionID = "inst1";
        Principal principal = mock(Principal.class);
        CourseRequest courseRequest = CourseRequest.builder().build();
        courseRequest.setName("New Course");
        courseRequest.setDescription("Course Description");
        courseRequest.setCredit(3);
        courseRequest.setTeacher("teacher@example.com");

        Institution institution = new Institution();
        institution.setId(institutionID);
        when(institutionRepository.findById(institutionID)).thenReturn(Optional.of(institution));

        User teacher = new User();
        teacher.setEmail("teacher@example.com");
        when(userRepository.findUserByEmail("teacher@example.com")).thenReturn(teacher);

        // Act
        ResponseEntity<HttpStatus> response = courseService.createCourse(institutionID, courseRequest, principal);

        // Assert
        verify(courseRepository, times(1)).save(any(Course.class));
        verify(institutionRepository, times(1)).save(institution);
        verify(userRepository, times(1)).save(teacher);
        assertEquals(HttpStatus.OK, response.getBody());
    }

    @Test
    void updateCourse_ShouldUpdateCourseSuccessfully() {
        // Arrange
        String courseID = "course1";
        CourseRequest courseRequest = CourseRequest.builder().build();
        courseRequest.setName("Updated Course");
        courseRequest.setDescription("Updated Description");
        courseRequest.setCredit(4);

        Course course = Course.builder().build();
        course.setId(courseID);
        when(courseRepository.findById(courseID)).thenReturn(Optional.of(course));

        // Act
        ResponseEntity<HttpStatus> response = courseService.updateCourse(courseID, courseRequest);

        // Assert
        verify(courseRepository, times(1)).save(course);
        assertEquals(HttpStatus.OK, response.getBody());
    }

    @Test
    void deleteCourse_ShouldDeleteCourseSuccessfully() {
        // Arrange
        String courseID = "course1";
        Course course =  Course.builder().build();
        course.setId(courseID);
        course.setTeacher("teacher@example.com");
        course.setInstitutionID("inst1");

        when(courseRepository.findById(courseID)).thenReturn(Optional.of(course));

        Institution institution = new Institution();
        institution.setId("inst1");
        when(institutionRepository.findById("inst1")).thenReturn(Optional.of(institution));

        User teacher = new User();
        teacher.setEmail("teacher@example.com");
        when(userRepository.findUserByEmail("teacher@example.com")).thenReturn(teacher);

        // Act
        ResponseEntity<HttpStatus> response = courseService.deleteCourse(courseID);

        // Assert
        verify(courseRepository, times(1)).delete(course);
        verify(institutionRepository, times(1)).save(institution);
        verify(userRepository, times(1)).save(teacher);
        assertEquals(HttpStatus.OK, response.getBody());
    }

    @Test
    void getCourse_ShouldReturnCourseResponseSuccessfully() {
        // Arrange
        String courseID = "course1";
        Course course =  Course.builder().build();
        course.setId(courseID);
        course.setName("Test Course");
        course.setDescription("Test Description");
        course.setCredit(3);

        when(courseRepository.findById(courseID)).thenReturn(Optional.of(course));

        // Act
        ResponseEntity<CourseResponse> response = courseService.getCourse(courseID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseID, response.getBody().getId());
        assertEquals("Test Course", response.getBody().getName());
    }
}

