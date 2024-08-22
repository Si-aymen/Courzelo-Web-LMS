package org.example.courzelo.models.RevisionEntities.revision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "filerevision")
public class FileMetadatarevision {
    @Id
    private String id;
    @Indexed
    private String fileName;
    @Indexed
    private String fileDownloadUri;
    @Indexed
    private String revisionId;
    @DBRef
    private QuizRevision quizRevision;

    @Transient // This field is not persisted to MongoDB
    private MultipartFile file;
}
