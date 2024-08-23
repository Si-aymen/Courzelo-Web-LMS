package org.example.courzelo.models.RevisionEntities.QizzRevision;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.example.courzelo.models.RevisionEntities.revision.Revision;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "QuizRevision")
public class QuizRevision {
    @Id
    private String id;

    @Indexed
    private String title;

    @DBRef
    private FileMetadatarevision fileMetadatarevision;

    @DBRef
    private List<QuestionRevision> questions;

    @DBRef
    private Revision revision;
}