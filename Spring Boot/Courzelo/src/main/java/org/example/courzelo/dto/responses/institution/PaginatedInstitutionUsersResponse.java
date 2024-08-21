package org.example.courzelo.dto.responses.institution;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedInstitutionUsersResponse {
    private List<InstitutionUserResponse> users;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
}
