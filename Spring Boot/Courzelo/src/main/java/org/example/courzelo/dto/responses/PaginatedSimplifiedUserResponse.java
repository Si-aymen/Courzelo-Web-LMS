package org.example.courzelo.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedSimplifiedUserResponse {
    private List<SimplifiedUserResponse> users;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;

    public PaginatedSimplifiedUserResponse(List<SimplifiedUserResponse> users, int currentPage, int totalPages, long totalItems, int itemsPerPage) {
        this.users = users;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.itemsPerPage = itemsPerPage;
    }
}
