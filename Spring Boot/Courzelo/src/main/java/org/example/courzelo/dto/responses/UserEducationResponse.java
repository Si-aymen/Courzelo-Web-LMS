package org.example.courzelo.dto.responses;

import lombok.Data;
import org.example.courzelo.models.UserEducation;


@Data
public class UserEducationResponse {
    private String institutionID;
    private String institutionName;

    public UserEducationResponse(UserEducation userEducation) {
        this.institutionID = userEducation.getInstitution()!= null ? userEducation.getInstitution().getId() : null;
        this.institutionName = userEducation.getInstitution()!= null ? userEducation.getInstitution().getName() : null;
    }
}
