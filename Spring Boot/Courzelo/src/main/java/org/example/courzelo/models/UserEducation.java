package org.example.courzelo.models;

import lombok.Data;
import org.example.courzelo.models.institution.Institution;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public class UserEducation {
    @DBRef
    private Institution institution;
}
