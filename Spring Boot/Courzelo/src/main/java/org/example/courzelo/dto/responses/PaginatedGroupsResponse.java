package org.example.courzelo.dto.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedGroupsResponse {
    private List<GroupResponse> groups;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;

}