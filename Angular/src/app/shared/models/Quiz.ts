import {Question} from './Question';

export class Quiz {
    id?: string;
    title: string;
    description: string;
    questions: Question[] = [];
    isSelected: boolean;
    score: number;
    Status: string;
    duration: number;
    maxAttempts: number;
    category: string;
    constructor() {
        this.questions = [];
    }
}



