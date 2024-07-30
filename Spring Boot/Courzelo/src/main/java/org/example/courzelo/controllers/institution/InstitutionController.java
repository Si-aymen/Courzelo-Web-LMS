package org.example.courzelo.controllers.institution;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.example.courzelo.dto.requests.CalendarEventRequest;
import org.example.courzelo.dto.requests.InstitutionMapRequest;
import org.example.courzelo.dto.requests.InstitutionRequest;
import org.example.courzelo.dto.responses.StatusMessageResponse;
import org.example.courzelo.dto.responses.institution.InstitutionResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionUsersResponse;
import org.example.courzelo.dto.responses.institution.PaginatedInstitutionsResponse;
import org.example.courzelo.security.CustomAuthorization;
import org.example.courzelo.services.IInstitutionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/institution")
@AllArgsConstructor
@PreAuthorize("hasRole('SUPERADMIN')")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowedHeaders = "*", allowCredentials = "true")
public class InstitutionController {
    private final IInstitutionService iInstitutionService;
    private final CustomAuthorization customAuthorization;
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
    @PreAuthorize("hasRole('ADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
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
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<PaginatedInstitutionUsersResponse> getInstitutionUsers(@PathVariable @NotNull String institutionID,
                                                                                 @RequestParam(required = false) String keyword,
                                                                                 @RequestParam(required = false) String role,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int sizePerPage) {
        return iInstitutionService.getInstitutionUsers(institutionID,keyword, role, page, sizePerPage);
    }
    @PutMapping("/{institutionID}/invite_user")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<HttpStatus> inviteUser(@PathVariable @NotNull String institutionID,
                                                         @RequestParam @Email String email,
                                                         @RequestParam @NotNull String role,
                                                         Principal principal) {
        return iInstitutionService.inviteUser(institutionID, email, role, principal);
    }
    @PutMapping("/accept_invite/{code}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HttpStatus> acceptInvite(@PathVariable @NotNull String code) {
        return iInstitutionService.acceptInvite(code);
    }
    @PutMapping("/{institutionID}/add-user")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<HttpStatus> addInstitutionUser(@PathVariable @NotNull String institutionID,
                                                         @RequestParam @Email String email,
                                                         @RequestParam @NotNull String role,
                                                         Principal principal) {
        return iInstitutionService.addInstitutionUser(institutionID, email, role, principal);
    }
    @DeleteMapping("/{institutionID}/remove-user")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<HttpStatus> removeInstitutionUser(@PathVariable @NotNull String institutionID,
                                                            @RequestParam @Email String email,
                                                            Principal principal) {
        return iInstitutionService.removeInstitutionUser(institutionID, email, principal);
    }
    @PutMapping("/{institutionID}/remove-user-role")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<HttpStatus> removeInstitutionUserRole(@PathVariable @NotNull String institutionID,
                                                               @RequestParam @Email String email,
                                                               @RequestParam @NotNull String role,
                                                               Principal principal) {
        return iInstitutionService.removeInstitutionUserRole(institutionID, email, role, principal);
    }
    @PutMapping("/{institutionID}/add-user-role")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<HttpStatus> addInstitutionUserRole(@PathVariable @NotNull String institutionID,
                                                                @RequestParam @Email String email,
                                                                @RequestParam @NotNull String role,
                                                                Principal principal) {
        return iInstitutionService.addInstitutionUserRole(institutionID, email, role, principal);
    }
    @PutMapping("/{institutionID}/set-map")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<HttpStatus> setInstitutionMap(@PathVariable @NotNull String institutionID,
                                                        @RequestBody @Valid InstitutionMapRequest institutionMapRequest,
                                                        Principal principal) {
        return iInstitutionService.setInstitutionMap(institutionID, institutionMapRequest, principal);
    }
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    @PostMapping("/{institutionID}/generate-excel")
    public ResponseEntity<HttpStatus> generateExcel(@PathVariable @NotNull String institutionID, @RequestBody List<CalendarEventRequest> events, Principal principal) {
        return iInstitutionService.generateExcel(institutionID,events,principal);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{institutionID}/download-excel")
    public ResponseEntity<byte[]> downloadExcel(@PathVariable @NotNull String institutionID,Principal principal) {
        return iInstitutionService.downloadExcel(institutionID,principal);
    }
    @PostMapping("/{institutionID}/image")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')&&@customAuthorization.canAccessInstitution(#institutionID)")
    public ResponseEntity<HttpStatus> uploadInstitutionImage(@PathVariable @NotNull String institutionID,
                                                             @RequestParam("file") MultipartFile file, Principal principal) {
        return iInstitutionService.uploadInstitutionImage(institutionID,file, principal);
    }
    @GetMapping("/{institutionID}/image")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable @NotNull String institutionID,Principal principal) {
        return iInstitutionService.getInstitutionImage(institutionID,principal);
    }
}
