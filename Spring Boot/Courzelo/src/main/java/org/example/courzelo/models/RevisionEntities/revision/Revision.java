package org.example.courzelo.models.RevisionEntities.revision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Revision")
public class Revision {
    @Id
    private String id;

    @Indexed
    private String titre;

    @DBRef
    private List<FileMetadatarevision> files;

    private int nbrmax;

    @Indexed
    private SubjectRevision subjectRevision;

    @DBRef
    private List<QuizRevision> quizRevisions;
}