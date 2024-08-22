package org.example.courzelo.models.ProjectEntities.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "Tasks")
public class Tasks {
    @Id
    private  String id;

    @Indexed
    private String name;

    @Indexed
    private Status status;

    @DBRef
    @JsonIgnore
    private Project project;

    @DBRef
    private GroupProject groupProject;

    public void setStatus(Status status) {
        if (status == null) {
            this.status = Status.ToDo;
        } else {
            this.status = status;
        }
    }
}
