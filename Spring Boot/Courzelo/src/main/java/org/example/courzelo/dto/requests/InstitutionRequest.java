package org.example.courzelo.dto.requests;

import lombok.Data;

@Data
public class InstitutionRequest {
    private String name;
    private String slogan;
    private String country;
    private String address;
    private String description;
    private String website;
    private Double latitude;
    private Double longitude;
}
