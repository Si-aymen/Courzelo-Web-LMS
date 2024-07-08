import {QuestionType} from './QuestionType';

class Answer {
    id: string;
    questionID: string;
    answer: string;
}

export class Question {
    id: string;
    text: string;
    options: string[];
    correctAnswer: string;
    type: QuestionType;
    answers: Answer[];

}
