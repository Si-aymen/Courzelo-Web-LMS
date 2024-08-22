package org.example.courzelo.dto.RevisionDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadatarevisionDTO {

    private String id;

    private String fileName;

    private String fileDownloadUri;

    private String revisionId;

    private QuizRevisionDTO quizRevision;

    private MultipartFile file;
}
