package org.example.courzelo.controllers.institution;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.PaginatedSimplifiedUserResponse;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.example.courzelo.services.IInstitutionService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<StatusMessageResponse> addInstitution(@RequestBody @Valid InstitutionRequest institutionRequest) {
        return iInstitutionService.addInstitution(institutionRequest);
    }
    @PutMapping("/update/{institutionID}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<HttpStatus> updateInstitution(@PathVariable @NotNull String institutionID,
                                                                  @RequestBody InstitutionRequest institutionRequest,
                                                                   Principal principal) {
        return iInstitutionService.updateInstitutionInformation(institutionID, institutionRequest,principal);
    }
    @DeleteMapping("/delete/{institutionID}")
    public ResponseEntity<StatusMessageResponse> deleteInstitution(@PathVariable @NotNull String institutionID) {
        return iInstitutionService.deleteInstitution(institutionID);
    }
    @GetMapping("/{institutionID}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InstitutionResponse> getInstitutionByID(@PathVariable @NotNull String institutionID) {
        return iInstitutionService.getInstitutionByID(institutionID);
    }
    @GetMapping("/{institutionID}/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedSimplifiedUserResponse> getInstitutionUsers(@PathVariable @NotNull String institutionID,
                                                                               @RequestParam(required = false) String keyword,
                                                                               @RequestParam(required = false) String role,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int sizePerPage) {
        return iInstitutionService.getInstitutionUsers(institutionID,keyword, role, page, sizePerPage);
    }
    @PutMapping("/{institutionID}/add-user")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<HttpStatus> addInstitutionUser(@PathVariable @NotNull String institutionID,
                                                         @RequestParam @Email String email,
                                                         @RequestParam @NotNull String role,
                                                         Principal principal) {
        return iInstitutionService.addInstitutionUser(institutionID, email, role, principal);
    }
    @DeleteMapping("/{institutionID}/removeUser")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<HttpStatus> removeInstitutionUser(@PathVariable @NotNull String institutionID,
                                                            @RequestParam @Email String email,
                                                            Principal principal) {
        return iInstitutionService.removeInstitutionUser(institutionID, email, principal);
    }
    @PutMapping("/{institutionID}/updateUserRole")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<HttpStatus> updateInstitutionUserRole(@PathVariable @NotNull String institutionID,
                                                               @RequestParam @Email String email,
                                                               @RequestParam @NotNull String role,
                                                               Principal principal) {
        return iInstitutionService.updateInstitutionUserRole(institutionID, email, role, principal);
    }
}
