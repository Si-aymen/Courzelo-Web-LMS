package org.example.courzelo.models;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserEducation {
    private String institutionID;
    private List<String> coursesID = new ArrayList<>();
}
