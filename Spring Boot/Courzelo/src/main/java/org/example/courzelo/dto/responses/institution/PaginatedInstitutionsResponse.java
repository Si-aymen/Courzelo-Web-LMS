package org.example.courzelo.dto.responses.institution;

import lombok.Data;
import org.example.courzelo.dto.responses.UserResponse;

import java.util.List;
@Data
public class PaginatedInstitutionsResponse {
    private List<InstitutionResponse> institutions;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
}
