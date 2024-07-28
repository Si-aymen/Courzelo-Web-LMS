package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.courzelo.dto.requests.CalendarEventRequest;
import org.example.courzelo.dto.requests.InstitutionMapRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InstitutionServiceImpl implements IInstitutionService {
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final CalendarService calendarService;
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
            log.info("role is null");
            users = institution.getUsers();
        } else {
            log.info("role is not null");
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
                    .filter(user ->
                            Optional.ofNullable(user.getProfile().getName()).orElse("").toLowerCase().contains(keyword.toLowerCase()) ||
                                    Optional.ofNullable(user.getProfile().getLastname()).orElse("").toLowerCase().contains(keyword.toLowerCase()) ||
                                    Optional.ofNullable(user.getProfile().getTitle()).orElse("").toLowerCase().contains(keyword.toLowerCase()) ||
                                    Optional.ofNullable(user.getProfile().getCountry()).orElse("").toLowerCase().contains(keyword.toLowerCase()) ||
                                    user.getRoles().stream().anyMatch(userRole -> userRole.name().toLowerCase().contains(keyword.toLowerCase())) ||
                                    Optional.ofNullable(user.getProfile().getGender()).orElse("").toLowerCase().contains(keyword.toLowerCase()) ||
                                    Optional.ofNullable(user.getEmail()).orElse("").toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }
        log.info("after keyword");
        int start = page * sizePerPage;
        log.info("after keyword");
        int end = Math.min((start + sizePerPage), users.size());
        log.info("after keyword");
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
        log.info("Removing user from institution {} with email: {}", institutionID, email);
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        log.info("Institution found");
        User user = userRepository.findUserByEmail(email);
        log.info("User finding");
        if(user == null ){
            log.info("test");
            return ResponseEntity.notFound().build();
        }
        log.info("User found");
        if(isUserInInstitution(user, institution)){
            log.info("User in institution");
            if(institution.getAdmins().contains(user)){
                user.getRoles().remove(Role.ADMIN);
                institution.getAdmins().remove(user);
                log.info("User removed from admins");
            }
            if (institution.getTeachers().contains(user)){
                institution.getTeachers().remove(user);
                user.getRoles().remove(Role.TEACHER);
                log.info("User removed from teachers");
            }
            if (institution.getStudents().contains(user)){
                user.getRoles().remove(Role.STUDENT);
                institution.getStudents().remove(user);
                log.info("User removed from students");
            }
            user.getEducation().setInstitution(null);
            log.info("User removed from institution");
            userRepository.save(user);
            institutionRepository.save(institution);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Override
    public ResponseEntity<HttpStatus> addInstitutionUserRole(String institutionID, String email, String role, Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        User user = userRepository.findUserByEmail(email);
        if(user == null ){
            return ResponseEntity.notFound().build();
        }
        if(isUserInInstitution(user, institution)){
            if(role.equals("ADMIN")){
                if(institution.getAdmins().contains(user)){
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                institution.getAdmins().add(user);
                user.getRoles().add(Role.ADMIN);
            }else if(role.equals("TEACHER")) {
                if (institution.getTeachers().contains(user)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                institution.getTeachers().add(user);
                user.getRoles().add(Role.TEACHER);
            }else{
                if (institution.getStudents().contains(user)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                institution.getStudents().add(user);
                user.getRoles().add(Role.STUDENT);
            }
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
            return ResponseEntity.notFound().build();
        }
        if(isUserInInstitution(user, institution)){
            if(role.equals("ADMIN")){
                if(institution.getAdmins().contains(user)){
                    institution.getAdmins().remove(user);
                    user.getRoles().remove(Role.ADMIN);
                }
            }else if(role.equals("TEACHER")) {
                if (institution.getTeachers().contains(user)) {
                    institution.getTeachers().remove(user);
                    user.getRoles().remove(Role.TEACHER);
                }
            }else{
                if (institution.getStudents().contains(user)) {
                    institution.getStudents().remove(user);
                    user.getRoles().remove(Role.STUDENT);
                }
            }
            if(getUserRoleInInstitution(user, institution).isEmpty()){
                user.getEducation().setInstitution(null);
            }
            userRepository.save(user);
            institutionRepository.save(institution);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<HttpStatus> setInstitutionMap(String institutionID, InstitutionMapRequest institutionMapRequest, Principal principal) {
        log.info("Setting institution map for institution: {}", institutionID);
        log.info("Latitude: {}, Longitude: {}", institutionMapRequest.getLatitude(), institutionMapRequest.getLongitude());
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        institution.setLatitude(institutionMapRequest.getLatitude());
        institution.setLongitude(institutionMapRequest.getLongitude());
        institutionRepository.save(institution);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<HttpStatus> generateExcel(String institutionID, List<CalendarEventRequest> events, Principal principal) {
            Institution institution = institutionRepository.findById(institutionID).orElseThrow();
            try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                calendarService.createCalendarSheet(workbook, events);
                workbook.write(outputStream);
                institution.setExcelFile(outputStream.toByteArray());
                institutionRepository.save(institution);
                return ResponseEntity.ok().build();
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
    }

    @Override
    public ResponseEntity<byte[]> downloadExcel(String institutionID, Principal principal) {
            Institution institution = institutionRepository.findById(institutionID).orElseThrow();
            byte[] excelFile = institution.getExcelFile();
            if (excelFile != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=school-year-calendar.xlsx");
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(excelFile);
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
            return ResponseEntity.notFound().build();
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
        log.info("Checking if user {} is in institution {}",user.getId(),institution.getId());
        log.info("User is in insitution : {}", user.getEducation().getInstitution() != null && user.getEducation().getInstitution().getId().equals(institution.getId()));
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
