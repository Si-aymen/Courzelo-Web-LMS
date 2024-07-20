package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.*;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.example.courzelo.models.Institution;
import org.example.courzelo.models.Role;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IInstitutionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InstitutionServiceImpl implements IInstitutionService {
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    @Override
    public ResponseEntity<PaginatedInstitutionsResponse> getInstitutions(int page, int sizePerPage, String keyword) {
        log.info("Fetching institutions for page: {}, sizePerPage: {}", page, sizePerPage);
        PageRequest pageRequest = PageRequest.of(page, sizePerPage);
        Query query = new Query().with(pageRequest);

        if (keyword != null && !keyword.trim().isEmpty()) {
            log.info("Searching for institutions with keyword: {}", keyword);
            Criteria criteria = new Criteria().orOperator(
                    Criteria.where("name").regex(keyword, "i"),
                    Criteria.where("slogan").regex(keyword, "i"),
                    Criteria.where("country").regex(keyword, "i"),
                    Criteria.where("address").regex(keyword, "i"),
                    Criteria.where("description").regex(keyword, "i"),
                    Criteria.where("website").regex(keyword, "i")
            );
            query.addCriteria(criteria);
        }

        List<Institution> institutions = mongoTemplate.find(query, Institution.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Institution.class);

        List<InstitutionResponse> institutionResponses = institutions.stream()
                .map(InstitutionResponse::new)
                .toList();

        PaginatedInstitutionsResponse response = new PaginatedInstitutionsResponse();
        response.setInstitutions(institutionResponses);
        response.setCurrentPage(page);
        response.setTotalPages((int) Math.ceil((double) total / sizePerPage));
        response.setTotalItems(total);
        response.setItemsPerPage(sizePerPage);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<StatusMessageResponse> addInstitution(InstitutionRequest institutionRequest) {
        Institution institution = new Institution(institutionRequest);
        institutionRepository.save(institution);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution added successfully"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> updateInstitutionInformation(String institutionID, InstitutionRequest institutionRequest) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        institution.updateInstitution(institutionRequest);
        institutionRepository.save(institution);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution updated successfully"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> updateMyInstitutionInformation(Principal principal, InstitutionRequest institutionRequest) {
        User user = userRepository.findUserByEmail(principal.getName());
        Institution institution = institutionRepository.findById(user.getEducation().getInstitution().getId()).orElseThrow();
        institution.updateInstitution(institutionRequest);
        institutionRepository.save(institution);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution updated successfully"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> deleteInstitution(String institutionID) {
        removeAllInstitutionUsers(institutionRepository.findById(institutionID).orElseThrow());
        institutionRepository.deleteById(institutionID);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution deleted successfully"));
    }

    @Override
    public ResponseEntity<InstitutionResponse> getInstitutionByID(String institutionID) {
        return ResponseEntity.ok(new InstitutionResponse(institutionRepository.findById(institutionID).orElseThrow()));
    }


    @Override
    public ResponseEntity<PaginatedSimplifiedUserResponse> getInstitutionUsers(String institutionID, String keyword, String role, int page, int sizePerPage) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        List<User> users = switch (role) {
            case "ADMIN" -> institution.getAdmins();
            case "TEACHER" -> institution.getTeachers();
            case "STUDENT" -> institution.getStudents();
            default -> institution.getUsers();
        };
        if (keyword != null && !keyword.trim().isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getProfile().getName().toLowerCase().contains(keyword.toLowerCase()) ||
                            user.getProfile().getLastname().toLowerCase().contains(keyword.toLowerCase()) ||
                            user.getProfile().getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                            user.getProfile().getCountry().toLowerCase().contains(keyword.toLowerCase()) ||
                            user.getProfile().getGender().toLowerCase().contains(keyword.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }

        int start = page * sizePerPage;
        int end = Math.min((start + sizePerPage), users.size());
        List<SimplifiedUserResponse> simplifiedUserResponse = users.subList(start, end).stream()
                .map(SimplifiedUserResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PaginatedSimplifiedUserResponse(simplifiedUserResponse, page, (int) Math.ceil((double) users.size() / sizePerPage), users.size(), sizePerPage));
    }

    @Override
    public void removeAllInstitutionUsers(Institution institution) {
        institution.getAdmins().stream().peek(
                user -> user.getEducation().setInstitution(null)
        ).forEach(userRepository::save);
        institution.getTeachers().stream().peek(
                user -> user.getEducation().setInstitution(null)
        ).forEach(userRepository::save);
        institution.getStudents().stream().peek(
                user -> user.getEducation().setInstitution(null)
        ).forEach(userRepository::save);
    }

    @Override
    public ResponseEntity<HttpStatus> removeInstitutionUser(String institutionID, String email, Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        User admin = userRepository.findUserByEmail(principal.getName());
        if(!isUserSuperAdmin(admin) && !isUserAdminInInstitution(admin, institution)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findUserByEmail(email);
        if(user == null ){
            return ResponseEntity.badRequest().build();
        }
        if(isUserInInstitution(user, institution)){
            institution.getAdmins().remove(user);
            institution.getTeachers().remove(user);
            institution.getStudents().remove(user);
            user.getEducation().setInstitution(null);
            userRepository.save(user);
            institutionRepository.save(institution);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<HttpStatus> updateInstitutionUserRole(String institutionID, String email, String role, Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        User admin = userRepository.findUserByEmail(principal.getName());
        if(!isUserSuperAdmin(admin) && !isUserAdminInInstitution(admin, institution)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findUserByEmail(email);
        if(user == null || !isUserInInstitution(user, institution)){
            return ResponseEntity.badRequest().build();
        }
        switch (role) {
            case "ADMIN" -> {
                institution.getAdmins().add(user);
                institution.getTeachers().remove(user);
                institution.getStudents().remove(user);
                user.getRoles().add(Role.ADMIN);
                user.getRoles().remove(Role.TEACHER);
                user.getRoles().remove(Role.STUDENT);
            }
            case "TEACHER" -> {
                institution.getAdmins().remove(user);
                institution.getTeachers().add(user);
                institution.getStudents().remove(user);
                user.getRoles().add(Role.TEACHER);
                user.getRoles().remove(Role.ADMIN);
                user.getRoles().remove(Role.STUDENT);
            }
            case "STUDENT" -> {
                institution.getAdmins().remove(user);
                institution.getTeachers().remove(user);
                institution.getStudents().add(user);
                user.getRoles().add(Role.STUDENT);
                user.getRoles().remove(Role.ADMIN);
                user.getRoles().remove(Role.TEACHER);

            }
        }
        userRepository.save(user);
        institutionRepository.save(institution);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<HttpStatus> addInstitutionUser(String institutionID, String email, String role,Principal principal) {
        User admin = userRepository.findUserByEmail(principal.getName());
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        if(!isUserSuperAdmin(admin) && !isUserAdminInInstitution(admin, institution)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        User user = userRepository.findUserByEmail(email);
        if(user == null || isUserInInstitution(user, institution)){
            return ResponseEntity.badRequest().build();
        }
        switch (role) {
            case "ADMIN" -> institution.getAdmins().add(user);
            case "TEACHER" -> institution.getTeachers().add(user);
            case "STUDENT" -> institution.getStudents().add(user);
        }
        institutionRepository.save(institution);
        return ResponseEntity.ok().build();
    }

    public boolean isUserInInstitution(User user, Institution institution){
        return user.getEducation().getInstitution() != null && Objects.equals(user.getEducation().getInstitution().getId(), institution.getId());
    }
    public boolean isUserAdminInInstitution(User user, Institution institution){
        return institution.getAdmins().contains(user);
    }
    public boolean isUserSuperAdmin(User user){
        return user.getRoles().contains(Role.SUPERADMIN);
    }
}
