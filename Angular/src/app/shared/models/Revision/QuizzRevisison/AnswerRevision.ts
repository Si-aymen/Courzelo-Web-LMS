import { UserResponse } from "../../user/UserResponse";
import { QuestionRevision } from "./QuestionRevision";
import { QuizRevision } from "./QuizRevision ";

export interface AnswerRevision {
    id?: string;
    question: QuestionRevision;
    answerText: string;
    isCorrect: boolean;
    quizRevision: QuizRevision;
    user: UserResponse;
  }