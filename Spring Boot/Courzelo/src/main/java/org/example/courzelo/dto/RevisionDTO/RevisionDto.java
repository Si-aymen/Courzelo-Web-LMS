package org.example.courzelo.dto.RevisionDTO;

import org.example.courzelo.models.RevisionEntities.QizzRevision.QuizRevision;
import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.example.courzelo.models.RevisionEntities.revision.SubjectRevision;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevisionDto {

        private String id;
        private String titre;

        private List<FileMetadatarevisionDTO> files;

        private int nbrmax;

        private String subjectRevision;


        private List<QuizRevisionDTO> quizRevisions;
    }

