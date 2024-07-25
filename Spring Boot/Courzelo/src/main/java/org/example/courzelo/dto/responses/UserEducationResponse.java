package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.UserEducation;


@Data
public class UserEducationResponse {
    private String institution;

    public UserEducationResponse(UserEducation userEducation) {
        this.institution = userEducation.getInstitution()!= null ? userEducation.getInstitution().getId() : null;
    }
}
