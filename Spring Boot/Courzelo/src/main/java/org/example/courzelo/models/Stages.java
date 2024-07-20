package org.example.courzelo.models;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "stages")
public class Stages {
    @MongoId
    private String id;
    private String name;
    private String description;
    private Locations location;
    private StageType type;
    private StageStatus status;
    private String entName ;
    private StageDomain domain ;
    private long duration;
}
