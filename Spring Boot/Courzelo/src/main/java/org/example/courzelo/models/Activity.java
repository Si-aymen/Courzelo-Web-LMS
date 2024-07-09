package org.example.courzelo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "activities")
public class Activity {

    @MongoId
    private String id;

    private String name;
    private String description;
    private String category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")

    private Date date;
    private String time;
    private String location;
    private Status status;

    public enum Status {
        PLANIFIEE,
        EN_COURS,
        TERMINEE,
        ANNULEE
    }
}
