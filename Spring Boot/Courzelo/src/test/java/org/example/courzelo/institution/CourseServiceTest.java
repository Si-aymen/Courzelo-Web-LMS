package org.example.courzelo.institution;

import org.example.courzelo.dto.requests.CourseRequest;
import org.example.courzelo.models.User;
import org.example.courzelo.models.UserEducation;
import org.example.courzelo.models.institution.Course;
import org.example.courzelo.models.institution.Group;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.CourseRepository;
import org.example.courzelo.repositories.GroupRepository;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.CourseServiceImpl;
import org.example.courzelo.services.ICourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourse_successfullyCreatesCourse() {
        // Arrange
        String institutionID = "institution-id";
        CourseRequest courseRequest =CourseRequest.builder()
                .name("course-name")
                .description("course-description")
                .credit(3)
                .group("group-id")
                .teacher("teacher-email")
                .build();
        Institution institution = new Institution();
        institution.setId(institutionID);
        institution.setCoursesID(new ArrayList<>());

        Course course = Course.builder()
                .name(courseRequest.getName())
                .description(courseRequest.getDescription())
                .credit(courseRequest.getCredit())
                .institutionID(institutionID)
                .teacher(courseRequest.getTeacher())
                .group(courseRequest.getGroup())
                .posts(new ArrayList<>())
                .build();

        User teacher = new User();
        teacher.setEducation(new UserEducation());
        teacher.getEducation().setCoursesID(new ArrayList<>());

        Group group = Group.builder().build();
        group.setCourses(new ArrayList<>());

        when(institutionRepository.findById(institutionID)).thenReturn(Optional.of(institution));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(userRepository.findUserByEmail(courseRequest.getTeacher())).thenReturn(teacher);
        when(groupRepository.findById(courseRequest.getGroup())).thenReturn(Optional.of(group));

        // Act
        ResponseEntity<HttpStatus> response = courseService.createCourse(institutionID, courseRequest, principal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, institution.getCoursesID().size());
        assertEquals(1, teacher.getEducation().getCoursesID().size());
        assertEquals(1, group.getCourses().size());

        verify(courseRepository, times(1)).save(any(Course.class));
        verify(institutionRepository, times(1)).save(any(Institution.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(groupRepository, times(1)).save(any(Group.class));
    }
    @Test
    void updateCourse_successfullyUpdatesCourse() {
        // Arrange
        String courseID = "course-id";
        CourseRequest courseRequest = CourseRequest.builder()
                .name("Updated Course Name")
                .description("Updated Course Description")
                .credit(4)
                .build();
        Course course =Course.builder().build();
        course.setId(courseID);
        course.setName("Old Course Name");
        course.setDescription("Old Course Description");
        course.setCredit(3);

        when(courseRepository.findById(courseID)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Act
        ResponseEntity<HttpStatus> response = courseService.updateCourse(courseID, courseRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Course Name", course.getName());
        assertEquals("Updated Course Description", course.getDescription());
        assertEquals(4, course.getCredit());

        verify(courseRepository, times(1)).findById(courseID);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void deleteCourse_successfullyDeletesCourse() {
        // Arrange
        String courseID = "course-id";
        Course course =Course.builder()
                .group("group-id")
                .teacher("teacher-email")
                .institutionID("institution-id")
                .build();

        Group group = Group.builder().build();
        group.setId("group-id");
        group.setCourses(new ArrayList<>());
        group.getCourses().add(courseID);

        User teacher = new User();
        teacher.setEducation(new UserEducation());
        teacher.getEducation().setCoursesID(new ArrayList<>());
        teacher.getEducation().getCoursesID().add(courseID);

        Institution institution = new Institution();
        institution.setCoursesID(new ArrayList<>());
        institution.getCoursesID().add(courseID);

        when(courseRepository.findById(courseID)).thenReturn(Optional.of(course));
        when(groupRepository.findById(course.getGroup())).thenReturn(Optional.of(group));
        when(userRepository.findUserByEmail(course.getTeacher())).thenReturn(teacher);
        when(institutionRepository.findById(course.getInstitutionID())).thenReturn(Optional.of(institution));

        // Act
        ResponseEntity<HttpStatus> response = courseService.deleteCourse(courseID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, group.getCourses().size());
        assertEquals(1, teacher.getEducation().getCoursesID().size());
        assertEquals(1, institution.getCoursesID().size());

        verify(courseRepository, times(1)).findById(courseID);
        verify(groupRepository, times(1)).findById(course.getGroup());
        verify(userRepository, times(1)).findUserByEmail(course.getTeacher());
        verify(institutionRepository, times(1)).findById(course.getInstitutionID());
        verify(courseRepository, times(1)).delete(course);
    }
}

