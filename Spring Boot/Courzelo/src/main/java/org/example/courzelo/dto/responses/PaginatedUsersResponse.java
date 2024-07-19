package org.example.courzelo.dto.responses;

import lombok.Data;

import java.util.List;
@Data
public class PaginatedUsersResponse {
    private List<UserResponse> users;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;

}
