import { FileMetadatarevision } from "../FileMetadatarevision";
import { QuestionRevision } from "./QuestionRevision";
import { Revision } from "../Revision";

export interface QuizRevision {
    id: string;
    title: string;
    fileMetadatarevision: FileMetadatarevision;
    questions: QuestionRevision[];
    revision: Revision;
  }