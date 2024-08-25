package org.example.courzelo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;


import java.lang.annotation.Documented;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "transports")
public class Transports {
    @MongoId
    private String id;
    private Locations departure;
    private Locations destination;
    private Date time;
    private float price ;

}
