import { QuizRevision } from "./QuizzRevisison/QuizRevision ";

export interface FileMetadatarevision {
    id: number;
    fileName: string;
    fileDownloadUri: string;
    revisionId: string;
    quizRevision?: QuizRevision;

  }