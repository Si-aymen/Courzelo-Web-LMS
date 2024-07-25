package org.example.courzelo.services;

import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionUsersResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.example.courzelo.models.Institution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

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
}
