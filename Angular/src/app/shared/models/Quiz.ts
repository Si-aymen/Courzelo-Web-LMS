export class Quiz {
    title: string;
    description: string;
    questions: Question[] = [];
    isSelected: boolean;
    score: number;
    Status: string;

    constructor() {
        this.questions = [];
    }
}

export class Question {
    questionText: string;
    options: string[];
    correctAnswer: string;
}
