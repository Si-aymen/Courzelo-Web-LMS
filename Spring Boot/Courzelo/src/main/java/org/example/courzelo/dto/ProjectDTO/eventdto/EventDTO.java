package org.example.courzelo.dto.ProjectDTO.eventdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.dto.ProjectDTO.projectdto.ProjectDTO;
import org.example.courzelo.models.ProjectEntities.event.Color;
import org.example.courzelo.models.ProjectEntities.event.Meta;
import org.example.courzelo.models.ProjectEntities.event.Resizable;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private String id;
    private String title;
    private Date start;
    private Date end;
    private String primaryColor;
    private String secondaryColor;
    private String notes;
    private ProjectDTO project;
    private Color color;
    private Resizable resizable;
    private boolean draggable;
    private boolean allDay;
    private String cssClass;
    private Meta meta;

}
