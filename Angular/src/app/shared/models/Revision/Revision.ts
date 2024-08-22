import { FileMetadatarevision } from "./FileMetadatarevision";
import { QuizRevision } from "./QuizzRevisison/QuizRevision ";
import { subjectRevision } from "./SubjectRevision";

export interface Revision {

       id: string; 
      titre: string;
      nbrmax: number;
      subjectRevision: subjectRevision;
      quizzRevisions?: QuizRevision[];
      files?: FileMetadatarevision[];
      
    }