package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.*;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.InstitutionUserResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionUsersResponse;
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
import java.util.ArrayList;
import java.util.List;
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
        log.info("Total institutions found: -1");
        List<Institution> institutions = mongoTemplate.find(query, Institution.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Institution.class);
        log.info("Total institutions found: {}", total);
        List<InstitutionResponse> institutionResponses = institutions.stream()
                .map(InstitutionResponse::new)
                .toList();
        log.info("Total institutions found2: {}", total);
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
    public ResponseEntity<HttpStatus> updateInstitutionInformation(String institutionID, InstitutionRequest institutionRequest,Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        institution.updateInstitution(institutionRequest);
        institutionRepository.save(institution);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<PaginatedInstitutionUsersResponse> getInstitutionUsers(String institutionID, String keyword, String role, int page, int sizePerPage) {
        log.info("Fetching users for institution: {}, page: {}, sizePerPage: {}, keyword: {}, role: {}", institutionID, page, sizePerPage, keyword, role);
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        List<User> users;
        if (role == null) {
            users = institution.getUsers();
        } else {
            users = switch (role) {
                case "ADMIN" -> institution.getAdmins();
                case "TEACHER" -> institution.getTeachers();
                case "STUDENT" -> institution.getStudents();
                default -> institution.getUsers();
            };
        }
        log.info("after role");
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
        log.info("after keyword");
        int start = page * sizePerPage;
        int end = Math.min((start + sizePerPage), users.size());
        List<InstitutionUserResponse> institutionUserResponses = users.subList(start, end).stream()
                .map(user -> InstitutionUserResponse.builder()
                        .email(user.getEmail())
                        .name(user.getProfile().getName())
                        .lastname(user.getProfile().getLastname())
                        .roles(getUserRoleInInstitution(user, institution).stream().map(Role::name).toList())
                        .country(user.getProfile().getCountry())
                        .gender(user.getProfile().getGender())
                        .build())
                .collect(Collectors.toList());
        log.info("after response");
        return ResponseEntity.ok(PaginatedInstitutionUsersResponse.builder()
                .users(institutionUserResponses)
                .currentPage(page)
                .totalPages((int) Math.ceil((double) users.size() / sizePerPage))
                .totalItems(users.size())
                .itemsPerPage(sizePerPage)
                .build()
        );
    }

    @Override
    public void removeAllInstitutionUsers(Institution institution) {
        institution.getUsers().forEach(user -> {
            user.getEducation().setInstitution(null);
            userRepository.save(user);
        });
    }

    @Override
    public ResponseEntity<HttpStatus> removeInstitutionUser(String institutionID, String email, Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        User user = userRepository.findUserByEmail(email);
        if(user == null ){
            return ResponseEntity.badRequest().build();
        }
        if(isUserInInstitution(user, institution)){
            institution.getAdmins().remove(user);
                user.getRoles().remove(Role.ADMIN);
            institution.getTeachers().remove(user);
                user.getRoles().remove(Role.TEACHER);
            institution.getStudents().remove(user);
                user.getRoles().remove(Role.STUDENT);
            user.getEducation().setInstitution(null);
            userRepository.save(user);
            institutionRepository.save(institution);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<HttpStatus> removeInstitutionUserRole(String institutionID, String email, String role, Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        User user = userRepository.findUserByEmail(email);
        if(user == null ){
            return ResponseEntity.badRequest().build();
        }
        if(isUserInInstitution(user, institution)){
            switch (role) {
                case "ADMIN" -> {
                    institution.getAdmins().remove(user);
                    if(getUserRoleInInstitution(user, institution)==null){
                        user.getEducation().setInstitution(null);
                    }
                        user.getRoles().remove(Role.ADMIN);
                }
                case "TEACHER" -> {
                    institution.getTeachers().remove(user);
                    if(getUserRoleInInstitution(user, institution)==null){
                        user.getEducation().setInstitution(null);
                    }
                        user.getRoles().remove(Role.TEACHER);
                }
                case "STUDENT" -> {
                    institution.getStudents().remove(user);
                    if(getUserRoleInInstitution(user, institution)==null){
                        user.getEducation().setInstitution(null);
                    }
                        user.getRoles().remove(Role.STUDENT);

                }
            }
            userRepository.save(user);
            institutionRepository.save(institution);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<HttpStatus> addInstitutionUser(String institutionID, String email, String role,Principal principal) {
        log.info("Adding user to institution {} with email: {} and role : {} ", institutionID, email , role);
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            log.info("User not found");
            return ResponseEntity.badRequest().build();
        }
        if(isUserInInstitution(user, institution) && getUserRoleInInstitution(user, institution).contains(Role.valueOf(role))){
            log.info("User already in institution");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        switch (role) {
            case "ADMIN" -> {
                institution.getAdmins().add(user);
                addUserToInstitution(user, institution, Role.ADMIN);
            }
            case "TEACHER" -> {
                institution.getTeachers().add(user);
                addUserToInstitution(user, institution, Role.TEACHER);
            }
            case "STUDENT" -> {
                institution.getStudents().add(user);
                addUserToInstitution(user, institution, Role.STUDENT);
            }
        }
        institutionRepository.save(institution);
        return ResponseEntity.ok().build();
    }
    public void addUserToInstitution(User user, Institution institution, Role role){
        if(user.getEducation().getInstitution()==null) {
            log.info("Setting user institution");
            user.getEducation().setInstitution(institution);

            if (!user.getRoles().contains(role)) {
                user.getRoles().add(role);
            }
            userRepository.save(user);
        }
    }
    public boolean isUserInInstitution(User user, Institution institution){
        return user.getEducation().getInstitution() != null && user.getEducation().getInstitution().getId().equals(institution.getId());
    }
    public List<Role> getUserRoleInInstitution(User user, Institution institution){
        List<Role> roles= new ArrayList<>();
        if(institution.getAdmins().contains(user)){
            roles.add( Role.ADMIN);
        }
        if(institution.getTeachers().contains(user)){
            roles.add(Role.TEACHER);
        }
        if(institution.getStudents().contains(user)){
            roles.add(Role.STUDENT);
        }
        return roles;
    }
}
