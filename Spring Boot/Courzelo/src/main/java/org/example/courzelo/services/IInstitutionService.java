package org.example.courzelo.services;

import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.PaginatedSimplifiedUserResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface IInstitutionService {
    ResponseEntity<PaginatedInstitutionsResponse> getInstitutions(int page, int sizePerPage, String keyword);
    ResponseEntity<StatusMessageResponse> addInstitution(InstitutionRequest institutionRequest);
    ResponseEntity<StatusMessageResponse> updateInstitution(String institutionID, InstitutionRequest institutionRequest);
    ResponseEntity<StatusMessageResponse> updateMyInstitution(Principal principal, InstitutionRequest institutionRequest);
    ResponseEntity<StatusMessageResponse> deleteInstitution(String institutionID);
    ResponseEntity<InstitutionResponse> getInstitutionByID(String institutionID);
    ResponseEntity<InstitutionResponse> getMyInstitution(Principal principal);
    ResponseEntity<PaginatedSimplifiedUserResponse> getInstitutionUsers(String institutionID, String role, int page, int sizePerPage);
}
