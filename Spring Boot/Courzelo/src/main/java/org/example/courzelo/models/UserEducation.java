package org.example.courzelo.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserEducation {
    @DBRef
    private List<Institution> institutions= new ArrayList<>();
}
