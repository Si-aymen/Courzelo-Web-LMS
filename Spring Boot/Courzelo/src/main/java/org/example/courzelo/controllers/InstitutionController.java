package org.example.courzelo.controllers;

import lombok.AllArgsConstructor;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.PaginatedSimplifiedUserResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.example.courzelo.services.IInstitutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/institution")
@AllArgsConstructor
@PreAuthorize("hasRole('SUPERADMIN')")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
public class InstitutionController {
    private final IInstitutionService iInstitutionService;
    @GetMapping("/all")
    public ResponseEntity<PaginatedInstitutionsResponse> getInstitutions(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int sizePerPage,
                                                                         @RequestParam(required = false) String keyword) {
        return iInstitutionService.getInstitutions(page, sizePerPage, keyword);
    }
    @PostMapping("/add")
    public ResponseEntity<StatusMessageResponse> addInstitution(@RequestBody InstitutionRequest institutionRequest) {
        return iInstitutionService.addInstitution(institutionRequest);
    }
    @PutMapping("/update/{institutionID}")
    public ResponseEntity<StatusMessageResponse> updateInstitution(@PathVariable String institutionID,
                                                                  @RequestBody InstitutionRequest institutionRequest) {
        return iInstitutionService.updateInstitution(institutionID, institutionRequest);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatusMessageResponse> updateMyInstitution(Principal principal, @RequestBody InstitutionRequest institutionRequest) {
        return iInstitutionService.updateMyInstitution(principal,institutionRequest);
    }
    @DeleteMapping("/delete/{institutionID}")
    public ResponseEntity<StatusMessageResponse> deleteInstitution(@PathVariable String institutionID) {
        return iInstitutionService.deleteInstitution(institutionID);
    }
    @GetMapping("/{institutionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InstitutionResponse> getInstitutionByID(@PathVariable String institutionID) {
        return iInstitutionService.getInstitutionByID(institutionID);
    }
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public ResponseEntity<InstitutionResponse> getMyInstitution(Principal principal) {
        return iInstitutionService.getMyInstitution(principal);
    }
    @GetMapping("/{institutionID}/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedSimplifiedUserResponse> getInstitutionUsers(@PathVariable String institutionID,
                                                                               @RequestParam(required = false) String role,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int sizePerPage) {
        return iInstitutionService.getInstitutionUsers(institutionID, role, page, sizePerPage);
    }
}
