package org.example.courzelo.dto.requests;

import lombok.Data;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Data
public class InstitutionRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 255, message = "Slogan must be less than 255 characters")
    private String slogan;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 56, message = "Country must be between 2 and 56 characters")
    private String country;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @Size(min = 5, max = 100, message = "Email must be between 5 and 100 characters")
    private String website;
}
