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
import org.example.courzelo.models.CodeType;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.models.Role;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.ICodeVerificationService;
import org.example.courzelo.services.IInstitutionService;
import org.example.courzelo.services.IMailService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InstitutionServiceImpl implements IInstitutionService {
    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final MongoTemplate mongoTemplate;
    private final CalendarService calendarService;
    private final ICodeVerificationService codeVerificationService;
    private final IMailService mailService;
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
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
        institution.updateInstitution(institutionRequest);
        institutionRepository.save(institution);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<StatusMessageResponse> deleteInstitution(String institutionID) {
        removeAllInstitutionUsers(institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found")));
        institutionRepository.deleteById(institutionID);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution deleted successfully"));
    }

    @Override
    public ResponseEntity<InstitutionResponse> getInstitutionByID(String institutionID) {
        return ResponseEntity.ok(new InstitutionResponse(institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"))));
    }


    @Override
    public ResponseEntity<PaginatedInstitutionUsersResponse> getInstitutionUsers(String institutionID, String keyword, String role, int page, int sizePerPage) {
        log.info("Fetching users for institution: {}, page: {}, sizePerPage: {}, keyword: {}, role: {}", institutionID, page, sizePerPage, keyword, role);
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
        List<User> users;
        if (role == null) {
            log.info("role is null");
            users = institution.getUsers().stream()
                    .map(userRepository::findUserByEmail)
                    .filter(Objects::nonNull) // Filter out null users
                    .toList();
        } else {
            log.info("role is not null");
            users = switch (role) {
                case "ADMIN" -> institution.getAdmins().stream()
                        .map(userRepository::findUserByEmail)
                        .filter(Objects::nonNull) // Filter out null users
                        .toList();
                case "TEACHER" -> institution.getTeachers().stream()
                        .map(userRepository::findUserByEmail)
                        .filter(Objects::nonNull) // Filter out null users
                        .toList();
                case "STUDENT" -> institution.getStudents().stream()
                        .map(userRepository::findUserByEmail)
                        .filter(Objects::nonNull) // Filter out null users
                        .toList();
                default -> institution.getUsers().stream()
                        .map(userRepository::findUserByEmail)
                        .filter(Objects::nonNull) // Filter out null users
                        .toList();
            };
        }
        if (users.isEmpty()) {
            return ResponseEntity.ok(PaginatedInstitutionUsersResponse.builder()
                    .users(new ArrayList<>())
                    .currentPage(page)
                    .totalPages(0)
                    .totalItems(0)
                    .itemsPerPage(sizePerPage)
                    .build()
            );
        }
        log.info("users {}", users);
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
            User user1 = userRepository.findUserByEmail(user);
            user1.getEducation().setInstitutionID(null);
            userRepository.save(user1);
        });
    }

    @Override
    public ResponseEntity<HttpStatus> removeInstitutionUser(String institutionID, String email, Principal principal) {
        log.info("Removing user from institution {} with email: {}", institutionID, email);
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
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
            if(institution.getAdmins().contains(user.getEmail())){
                user.getRoles().remove(Role.ADMIN);
                institution.getAdmins().remove(user.getEmail());
                log.info("User removed from admins");
            }
            if (institution.getTeachers().contains(user.getEmail())){
                institution.getTeachers().remove(user.getEmail());
                user.getRoles().remove(Role.TEACHER);
                log.info("User removed from teachers");
            }
            if (institution.getStudents().contains(user.getEmail())){
                user.getRoles().remove(Role.STUDENT);
                institution.getStudents().remove(user.getEmail());
                log.info("User removed from students");
            }
            user.getEducation().setInstitutionID(null);
            log.info("User removed from institution");
            userRepository.save(user);
            institutionRepository.save(institution);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Override
    public ResponseEntity<HttpStatus> addInstitutionUserRole(String institutionID, String email, String role, Principal principal) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
        User user = userRepository.findUserByEmail(email);
        if(user == null ){
            return ResponseEntity.notFound().build();
        }
        if(isUserInInstitution(user, institution)){
            if(role.equals("ADMIN")){
                if(institution.getAdmins().contains(user.getEmail())){
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                institution.getAdmins().add(user.getEmail());
                user.getRoles().add(Role.ADMIN);
            }else if(role.equals("TEACHER")) {
                if (institution.getTeachers().contains(user.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                institution.getTeachers().add(user.getEmail());
                user.getRoles().add(Role.TEACHER);
            }else{
                if (institution.getStudents().contains(user.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                institution.getStudents().add(user.getEmail());
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
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
        User user = userRepository.findUserByEmail(email);
        if(user == null ){
            return ResponseEntity.notFound().build();
        }
        if(isUserInInstitution(user, institution)){
            if(role.equals("ADMIN")){
                if(institution.getAdmins().contains(user.getEmail())){
                    institution.getAdmins().remove(user.getEmail());
                    user.getRoles().remove(Role.ADMIN);
                }
            }else if(role.equals("TEACHER")) {
                if (institution.getTeachers().contains(user.getEmail())) {
                    institution.getTeachers().remove(user.getEmail());
                    user.getRoles().remove(Role.TEACHER);
                }
            }else{
                if (institution.getStudents().contains(user.getEmail())) {
                    institution.getStudents().remove(user.getEmail());
                    user.getRoles().remove(Role.STUDENT);
                }
            }
            if(getUserRoleInInstitution(user, institution).isEmpty()){
                user.getEducation().setInstitutionID(null);
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
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
        institution.setLatitude(institutionMapRequest.getLatitude());
        institution.setLongitude(institutionMapRequest.getLongitude());
        institutionRepository.save(institution);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<HttpStatus> generateExcel(String institutionID, List<CalendarEventRequest> events, Principal principal) {
            Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
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
            Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
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
    public ResponseEntity<HttpStatus> uploadInstitutionImage(String institutionID, MultipartFile file, Principal principal) {
        try {
            Institution institution= institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
            // Define the path where you want to save the image
            String baseDir = "upload" + File.separator + institution.getName() + File.separator + "logo" + File.separator;

            // Create the directory if it doesn't exist
            File dir = new File(baseDir);
            if (!dir.exists()) {
                boolean dirsCreated = dir.mkdirs();
                if (!dirsCreated) {
                    throw new IOException("Failed to create directories");
                }
            }
            // Get the original file name
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            // Generate a random filename
            String newFileName = UUID.randomUUID() + extension;
            // Define the path to the new file
            String filePath = baseDir + newFileName;
            log.info("File path: " + filePath);
            Files.copy(file.getInputStream(), new File(filePath).toPath());
            // Save the file to the server
            //file.transferTo(new File(filePath));
            //delete old image
            if(institution.getLogo() != null)
            {
                File oldImage = new File(institution.getLogo());
                if(oldImage.exists())
                {
                    oldImage.delete();
                }
            }
            // Save the file path and name in the user's profile
            institution.setLogo(filePath);
            // Save the user
            institutionRepository.save(institution);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error uploading image: " + e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<byte[]> getInstitutionImage(String institutionID, Principal principal) {
        try {
            // Get the user
            Institution instituion = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
            if(instituion.getLogo() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            String filePath = instituion.getLogo();
            // Read the file
            byte[] image = Files.readAllBytes(new File(filePath).toPath());
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            log.error("Error getting image: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> inviteUser(String institutionID, String email, String role, Principal principal) {
        log.info("Inviting user to institution {} with email: {} and role : {} ", institutionID, email , role);
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
        User user = userRepository.findUserByEmail(email);

        CodeVerification codeVerification = codeVerificationService.saveCode(
                CodeType.INSTITUTION_INVITATION,
                codeVerificationService.generateCode(),
                email,
                Role.valueOf(role),
                institutionID,
                Instant.now().plusSeconds(3600)
        );
        if(user == null){
            log.info("User not found");
            mailService.sendInstituionInvitationEmail(email, institution,codeVerification);
        }else {
            mailService.sendInstituionInvitationEmail(user, institution, codeVerification);
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<HttpStatus> acceptInvite(String code,Principal principal) {
        log.info("Accepting invite with code: {}", code);
        CodeVerification codeVerification = codeVerificationService.getCodeByCode(code);
        log.info("Code verification: {}", codeVerification);
        log.info("Code type is institution invitation");
        Institution institution = institutionRepository.findById(codeVerification.getInstitutionID()).orElseThrow();
        User user = userRepository.findUserByEmail(codeVerification.getEmail());
        log.info("Removing user from old institution");
        if(isUserInInstitution(user, institution) && getUserRoleInInstitution(user, institution).contains(codeVerification.getRole())){
            log.info("User already in institution");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if(user.getEducation().getInstitutionID()!=null && !isUserInInstitution(user, institution)){
            removeInstitutionUser(codeVerification.getInstitutionID(), codeVerification.getEmail(), null);
        }
        addInstitutionToUser(user, institution, codeVerification.getRole());
        addUserToInstitution(user, institution, codeVerification.getRole());
        log.info("Deleting code");
        codeVerificationService.deleteCode(codeVerification.getEmail(), CodeType.INSTITUTION_INVITATION);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<HttpStatus> addInstitutionUser(String institutionID, String email, String role,Principal principal) {
        log.info("Adding user to institution {} with email: {} and role : {} ", institutionID, email , role);
        Institution institution = institutionRepository.findById(institutionID).orElseThrow(()-> new NoSuchElementException("Institution not found"));
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            log.info("User not found");
            return ResponseEntity.notFound().build();
        }
        if(isUserInInstitution(user, institution) && getUserRoleInInstitution(user, institution).contains(Role.valueOf(role))){
            log.info("User already in institution");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        addUserToInstitution(user, institution, Role.valueOf(role));
        addInstitutionToUser(user, institution, Role.valueOf(role));
        return ResponseEntity.ok().build();
    }
    public void addInstitutionToUser(User user, Institution institution, Role role){
        if(user.getEducation().getInstitutionID()==null) {
            log.info("Setting user institution");
            user.getEducation().setInstitutionID(institution.getId());

            if (!user.getRoles().contains(role)) {
                user.getRoles().add(role);
            }
            userRepository.save(user);
        }
    }
    public void addUserToInstitution(User user, Institution institution, Role role){
        switch (role) {
            case ADMIN -> institution.getAdmins().add(user.getEmail());
            case TEACHER -> institution.getTeachers().add(user.getEmail());
            case STUDENT -> institution.getStudents().add(user.getEmail());
        }
        institutionRepository.save(institution);
    }

    public boolean isUserInInstitution(User user, Institution institution){
        log.info("Checking if user {} is in institution {}",user.getId(),institution.getId());
        log.info("User is in insitution : {}", user.getEducation().getInstitutionID() != null && user.getEducation().getInstitutionID().equals(institution.getId()));
        return user.getEducation().getInstitutionID() != null && user.getEducation().getInstitutionID().equals(institution.getId());
    }
    public List<Role> getUserRoleInInstitution(User user, Institution institution){
        List<Role> roles= new ArrayList<>();
        if(institution.getAdmins().contains(user.getEmail())){
            roles.add(Role.ADMIN);
        }
        if(institution.getTeachers().contains(user.getEmail())){
            roles.add(Role.TEACHER);
        }
        if(institution.getStudents().contains(user.getEmail())){
            roles.add(Role.STUDENT);
        }
        return roles;
    }
}
