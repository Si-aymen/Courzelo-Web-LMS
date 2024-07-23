package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.Institution;
import org.example.courzelo.models.UserEducation;

import java.util.List;

@Data
public class UserEducationResponse {
    private List<String> institutions;

    public UserEducationResponse(UserEducation userEducation) {
        this.institutions = userEducation.getInstitutions().stream().map(Institution::getId).toList();
    }
}
