package org.example.courzelo.dto.responses.institution;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InstitutionUserResponse {
    private String email;
    private String name;
    private String lastname;
    private List<String> roles;
    private String country;
    private String gender;
}
