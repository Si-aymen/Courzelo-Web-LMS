package org.example.courzelo.models.ProjectEntities.ChatProject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;
import org.example.courzelo.models.ProjectEntities.Project;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Chat")
public class Chat {
    @Id
    private String id;
    @Indexed
    private String text;

    @Indexed
    private String contactId;
    @Indexed
    private Date time ;
    
    @DBRef
    private Project project;

}
