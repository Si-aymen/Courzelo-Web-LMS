
import { UserResponse } from "../../user/UserResponse";
import { AnswerRevision } from "./AnswerRevision";
import { QuizRevision } from "./QuizRevision ";

export interface QuizrevisionResponse {
    id: string;
    quiz: QuizRevision;
    user: UserResponse;
    answers: AnswerRevision[];
    score: number;
  }