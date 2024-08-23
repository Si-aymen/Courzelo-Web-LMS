package org.example.courzelo.dto.RevisionDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.courzelo.models.RevisionEntities.QizzRevision.QuestionRevision;
import org.example.courzelo.models.RevisionEntities.revision.FileMetadatarevision;
import org.example.courzelo.models.RevisionEntities.revision.Revision;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizRevisionDTO {


        private String id;

        private String title;

        private FileMetadatarevisionDTO fileMetadatarevision;

        private List<QuestionRevisionsDto> questions;

        private RevisionDto revision;
    }


