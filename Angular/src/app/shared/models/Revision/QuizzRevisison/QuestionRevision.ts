import { AnswerRevision } from "./AnswerRevision";
import { QuestionType } from "./QuestionType ";
import { QuizRevision } from "./QuizRevision ";


export interface QuestionRevision {
    id: string;
    text: string;
    type: QuestionType;
    answerSubmitted?: boolean;
    correctAnswer: string; // Optional
    quizRevision: QuizRevision;
    answers: AnswerRevision[];
    userAnswerText?: string;
    isCorrect?: boolean; 

  }