package org.example.courzelo.models.ProjectEntities.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "file")
public class FileMetadata {
    @Id
    private String id;
    @Indexed
    private String fileName;
    @Indexed
    private String fileDownloadUri;
    @Indexed
    private String projectId;
}
