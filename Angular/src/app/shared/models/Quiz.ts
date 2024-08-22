import {Question} from './Question';
import {status} from './status';



export class Quiz {
    id?: string;
    title: string;
    description: string;
    questions: Question[] = [];
    isSelected: boolean;
    score: number;
    status: status;
    duration: number;
    maxAttempts: number;
    category: string;
}
export class QuizSubmission {
    quizID: string;
    answers: { questionID: string, answer: string }[] = [];
}
export class QuizSubmissionResult {
    quizID: string;
    score: number;
    maxScore: number;
    passed: boolean;
    status: status;
    submittedAt: Date;
    Answers: { questionID: string, answer: string }[] = [];
}


