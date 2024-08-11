package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.GroupRequest;
import org.example.courzelo.dto.responses.GroupResponse;
import org.example.courzelo.dto.responses.PaginatedGroupsResponse;
import org.example.courzelo.models.User;
import org.example.courzelo.models.institution.Group;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.repositories.CourseRepository;
import org.example.courzelo.repositories.GroupRepository;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GroupServiceImpl implements IGroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final InstitutionRepository institutionRepository;
    private final MongoTemplate mongoTemplate;
    @Override
    public ResponseEntity<GroupResponse> getGroup(String groupID) {
        Group group = groupRepository.findById(groupID).orElseThrow(() -> new NoSuchElementException("Group not found"));
        return ResponseEntity.ok(GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .students(group.getStudents())
                .courses(group.getCourses())
                .build());
    }

    @Override
    public ResponseEntity<PaginatedGroupsResponse> getGroupsByInstitution(String institutionID, int page, int sizePerPage) {
        Pageable pageable = PageRequest.of(page, sizePerPage);
        Query query = new Query(Criteria.where("institutionID").is(institutionID)).with(pageable);

        List<Group> groups = mongoTemplate.find(query, Group.class);
        long total = mongoTemplate.count(query.skip(-1).limit(-1), Group.class);

        List<GroupResponse> groupResponses = groups.stream()
                .map(group -> GroupResponse.builder()
                        .id(group.getId())
                        .name(group.getName())
                        .students(group.getStudents())
                        .courses(group.getCourses())
                        .build())
                .collect(Collectors.toList());

        log.info("Groups fetched successfully {}", groupResponses);

        return ResponseEntity.ok(PaginatedGroupsResponse.builder()
                .groups(groupResponses)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) total / sizePerPage))
                .totalItems(total)
                        .itemsPerPage(sizePerPage)
                .build());
    }

    @Override
    public ResponseEntity<HttpStatus> createGroup(GroupRequest groupRequest) {
        Group group = Group.builder()
                .name(groupRequest.getName())
                .institutionID(groupRequest.getInstitutionID())
                .students(groupRequest.getStudents())
                .courses(groupRequest.getCourses())
                .build();
        groupRepository.save(group);
        groupRequest.getStudents().forEach(
                studentEmail -> addGroupToUser(group.getId(), studentEmail)
        );
        groupRequest.getCourses().forEach(
                courseID -> addGroupToCourse(group.getId(), courseID)
        );
        return ResponseEntity.ok(HttpStatus.CREATED);

    }
    private void addGroupToUser(String groupID, String userEmail) {
        userRepository.findByEmail(userEmail).ifPresent(user -> {
            user.getEducation().setGroupID(groupID);
            userRepository.save(user);
        });
    }
    private void addGroupToCourse(String groupID, String courseID) {
        courseRepository.findById(courseID).ifPresent(course -> {
            course.setGroup(groupID);
            courseRepository.save(course);
        });
    }
    @Override
    public ResponseEntity<HttpStatus> updateGroup(String groupID, GroupRequest groupRequest) {
        Group group = groupRepository.findById(groupID).orElseThrow(() -> new NoSuchElementException("Group not found"));
        group.setName(groupRequest.getName());
        group.setStudents(groupRequest.getStudents());
        group.setCourses(groupRequest.getCourses());
        groupRequest.getStudents().forEach(
                studentEmail -> addGroupToUser(group.getId(), studentEmail)
        );
        groupRequest.getCourses().forEach(
                courseID -> addGroupToCourse(group.getId(), courseID)
        );
        groupRepository.save(group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteGroup(String groupID) {
        Group group = groupRepository.findById(groupID).orElseThrow(() -> new NoSuchElementException("Group not found"));
        if (group.getStudents() != null) {
            group.getStudents().forEach(
                    this::removeGroupFromUser
            );
        }
        if (group.getCourses() != null) {
            group.getCourses().forEach(
                    this::removeGroupFromCourse
            );
        }
        Institution institution = institutionRepository.findById(group.getInstitutionID()).orElseThrow(() -> new NoSuchElementException("Institution not found"));
        institution.getGroupsID().remove(groupID);
        institutionRepository.save(institution);
        groupRepository.delete(group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private void removeGroupFromUser(String studentEmail) {
        userRepository.findByEmail(studentEmail).ifPresent(user -> {
            user.getEducation().setGroupID(null);
            userRepository.save(user);
        });
    }
    private void removeGroupFromCourse(String courseID) {
        courseRepository.findById(courseID).ifPresent(course -> {
            course.setGroup(null);
            courseRepository.save(course);
        });
    }

    @Override
    public ResponseEntity<HttpStatus> addStudentToGroup(String groupID, String student) {
        Group group = groupRepository.findById(groupID).orElseThrow(() -> new NoSuchElementException("Group not found"));
        if(group.getStudents().contains(student)) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        log.info("Adding user {} to group {}", student, groupID);
        User user = userRepository.findByEmail(student).orElseThrow(() -> new NoSuchElementException("User not found"));
        if(!Objects.equals(user.getEducation().getInstitutionID(), group.getInstitutionID()))
        {
            log.info("User {} is not in the same institution as group {}", student, groupID);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        group.getStudents().add(user.getEmail());
        user.getEducation().setGroupID(groupID);
        groupRepository.save(group);
        userRepository.save(user);
        log.info("User {} added to group {}", student, groupID);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<HttpStatus> removeStudentFromGroup(String groupID, String studentID) {
        log.info("Removing user {} from group {}", studentID, groupID);
        User user = userRepository.findByEmail(studentID).orElseThrow(() -> new NoSuchElementException("User not found"));
        Group group = groupRepository.findById(groupID).orElseThrow(() -> new NoSuchElementException("Group not found"));
        if(!group.getStudents().contains(user.getEmail())) {
            return ResponseEntity.ok(HttpStatus.OK);
        }
        log.info("Removing user {} from group {}", studentID, groupID);
        group.getStudents().remove(user.getEmail());
        user.getEducation().setGroupID(null);
        groupRepository.save(group);
        userRepository.save(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public void deleteGroupsByInstitution(String institutionID) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(() -> new NoSuchElementException("Institution not found"));
        institution.getGroupsID().forEach(
                this::deleteGroup
        );
    }
}
