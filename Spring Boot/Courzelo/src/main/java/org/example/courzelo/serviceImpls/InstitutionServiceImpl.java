package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.*;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.example.courzelo.models.Institution;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.InstitutionRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.services.IInstitutionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

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
                    Criteria.where("country").regex(keyword, "i")
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
    public ResponseEntity<StatusMessageResponse> updateInstitution(String institutionID, InstitutionRequest institutionRequest) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        institution.updateInstitution(institutionRequest);
        institutionRepository.save(institution);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution updated successfully"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> updateMyInstitution(Principal principal, InstitutionRequest institutionRequest) {
        User user = userRepository.findUserByEmail(principal.getName());
        Institution institution = institutionRepository.findById(user.getEducation().getInstitution().getId()).orElseThrow();
        institution.updateInstitution(institutionRequest);
        institutionRepository.save(institution);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution updated successfully"));
    }

    @Override
    public ResponseEntity<StatusMessageResponse> deleteInstitution(String institutionID) {
        institutionRepository.deleteById(institutionID);
        return ResponseEntity.ok(new StatusMessageResponse("Success","Institution deleted successfully"));
    }

    @Override
    public ResponseEntity<InstitutionResponse> getInstitutionByID(String institutionID) {
        return ResponseEntity.ok(new InstitutionResponse(institutionRepository.findById(institutionID).orElseThrow()));
    }

    @Override
    public ResponseEntity<InstitutionResponse> getMyInstitution(Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName());
        Institution institution = institutionRepository.findById(user.getEducation().getInstitution().getId()).orElseThrow();
        return ResponseEntity.ok(new InstitutionResponse(institution));
    }

    @Override
    public ResponseEntity<PaginatedSimplifiedUserResponse> getInstitutionUsers(String institutionID, String role, int page, int sizePerPage) {
        Institution institution = institutionRepository.findById(institutionID).orElseThrow();
        List<User> users = switch (role) {
            case "ADMIN" ->
                    institution.getAdmins();
            case "TEACHER" ->
                    institution.getTeachers();
            case "STUDENT" ->
                    institution.getStudents();
            default -> institution.getUsers();
        };
        int start = page * sizePerPage;
        int end = Math.min((start + sizePerPage), users.size());
        List<SimplifiedUserResponse> simplifiedUserResponse = users.subList(start, end).stream()
                .map(SimplifiedUserResponse::new)
                .toList();
        return ResponseEntity.ok(new PaginatedSimplifiedUserResponse(simplifiedUserResponse, page, (int) Math.ceil((double) users.size() / sizePerPage), users.size(), sizePerPage));

    }
}
