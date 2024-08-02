package org.example.courzelo.models.ProjectEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Event")
public class Event {
    @Id
    private String id;
    @Indexed
    private String title;
    @Indexed
    private Date start;
    @Indexed
    private Date end;
    @Indexed
    private String primaryColor;
    @Indexed
    private String secondaryColor;
    @Indexed
    private String notes;
    @DBRef
    private Project project;
    @Indexed
    private Color color;
    @Indexed
    private Resizable resizable;

    @Indexed
    private boolean draggable;

    @Indexed
    private boolean allDay;

    private String cssClass;


    @Indexed
    private Meta meta;

}
