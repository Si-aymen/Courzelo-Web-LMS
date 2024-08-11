package org.example.courzelo.services;

import org.example.courzelo.dto.requests.CalendarEventRequest;
import org.example.courzelo.dto.requests.InstitutionMapRequest;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionUsersResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.example.courzelo.models.institution.Institution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface IInstitutionService {
    ResponseEntity<PaginatedInstitutionsResponse> getInstitutions(int page, int sizePerPage, String keyword);
    ResponseEntity<StatusMessageResponse> addInstitution(InstitutionRequest institutionRequest);
    ResponseEntity<HttpStatus> updateInstitutionInformation(String institutionID, InstitutionRequest institutionRequest,Principal principal);
    ResponseEntity<StatusMessageResponse> deleteInstitution(String institutionID);
    ResponseEntity<InstitutionResponse> getInstitutionByID(String institutionID);
    ResponseEntity<PaginatedInstitutionUsersResponse> getInstitutionUsers(String institutionID, String keyword, String role, int page, int sizePerPage);
    void removeAllInstitutionUsers(Institution institution);
    ResponseEntity<HttpStatus> addInstitutionUser(String institutionID, String email, String role,Principal principal);
    ResponseEntity<HttpStatus> removeInstitutionUser(String institutionID, String email,Principal principal);
    ResponseEntity<HttpStatus> addInstitutionUserRole(String institutionID, String email, String role, Principal principal);
    ResponseEntity<HttpStatus> removeInstitutionUserRole(String institutionID, String email, String role, Principal principal);

    ResponseEntity<HttpStatus> setInstitutionMap(String institutionID, InstitutionMapRequest institutionMapRequest, Principal principal);

    ResponseEntity<HttpStatus> generateExcel(String institutionID, List<CalendarEventRequest> events, Principal principal);

    ResponseEntity<byte[]> downloadExcel(String institutionID, Principal principal);

    ResponseEntity<HttpStatus> uploadInstitutionImage(String institutionID, MultipartFile file, Principal principal);

    ResponseEntity<byte[]> getInstitutionImage(String institutionID, Principal principal);

    ResponseEntity<HttpStatus> inviteUser(String institutionID, String email, String role, Principal principal);

    ResponseEntity<HttpStatus> acceptInvite(String code,Principal principal);

    ResponseEntity<List<String>> getInstitutionStudents(String institutionID);
}
