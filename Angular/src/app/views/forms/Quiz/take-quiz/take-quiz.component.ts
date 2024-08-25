import {Component, OnInit} from '@angular/core';
import {Quiz} from '../../../../shared/models/Quiz';
import {ActivatedRoute} from '@angular/router';
import {QuizService} from '../../../../shared/services/quiz.service';
import {SharedAnimations} from '../../../../shared/animations/shared-animations';
import {ToastrService} from 'ngx-toastr';
import {switchMap} from 'rxjs/operators';
import {of} from 'rxjs';

@Component({
  selector: 'app-take-quiz',
  templateUrl: './take-quiz.component.html',
  styleUrls: ['./take-quiz.component.scss'],
  animations: [SharedAnimations]
})
export class TakeQuizComponent implements OnInit {
    quizzes: Quiz[] = [];
    currentQuizIndex = 0;
    selectedAnswers: { [quizIndex: number]: { [questionId: string]: string[] | string } } = {};
    quizSubmissionStatus: { [key: string]: boolean } = {};
    showSummary = false;

    finalScore = 0;

    constructor(
        private quizService: QuizService,
        private toastr: ToastrService,
        private route: ActivatedRoute
    ) {}

    ngOnInit(): void {
        // Fetch all quizzes initially
        this.fetchAllQuizzes();
        this.initializeSelectedAnswers();
    }
    initializeSelectedAnswers(): void {
        this.quizzes.forEach((quiz, quizIndex) => {
            this.selectedAnswers[quizIndex] = {};
            quiz.questions.forEach(question => {
                this.selectedAnswers[quizIndex][question.id] =
                    question.type === 'MULTIPLE_CHOICE' ? [] : '';
            });
        });
    }
    validateQuizAnswers(): boolean {
        const currentQuiz = this.quizzes[this.currentQuizIndex];
        if (!currentQuiz) { return false; }

        return currentQuiz.questions.every(question => {
            const answer = this.selectedAnswers[this.currentQuizIndex]?.[question.id];
            console.log('Validating answer for question:', question);
            if (question.type === 'MULTIPLE_CHOICE') {
                return Array.isArray(answer) && answer.length > 0;
            } else {
                return typeof answer === 'string' && answer.trim().length > 0;
            }
        });
    }



    fetchAllQuizzes(): void {
        this.quizService.getAllQuizzes().subscribe(
            (quizzes: Quiz[]) => {
                if (quizzes && quizzes.length > 0) {
                    this.quizzes = quizzes;
                    this.initializeSelectedAnswers();
                } else {
                    this.toastr.error('No quizzes found', 'Error');
                }
            },
            error => {
                this.toastr.error('Failed to load quizzes', 'Error');
            }
        );
    }

    /*selectMultipleChoiceAnswer(questionId: string, answer: string): void {
        console.log(`Selecting answer for multiple choice question ID ${questionId}: ${answer}`);
        this.selectedAnswers[questionId] = answer;
    }*/
    selectMultipleChoiceAnswer(questionId: string, selectedOption: any) {
        this.selectedAnswers[questionId] = selectedOption;
    }

    submitQuiz(): void {
        if (!this.validateQuizAnswers()) {
            this.toastr.error('Please answer all questions before submitting the quiz.', 'Validation Error');
            return;
        }

        const currentQuiz = this.quizzes[this.currentQuizIndex];
        if (currentQuiz && currentQuiz.questions) {
            console.log('Submitting quiz:', currentQuiz);
            console.log('Selected answers:', this.selectedAnswers);
            this.quizSubmissionStatus[currentQuiz.id] = true;
            this.toastr.success('Quiz submitted successfully', 'Success');

            if (this.currentQuizIndex === this.quizzes.length - 1) {
                this.showSummary = true;
                this.calculateScore();
            } else {
                this.currentQuizIndex++;
            }
        }
    }

    loadQuizzes(): void {
        this.quizzes = [ /* Your quizzes data here */ ];
    }
    resetQuiz(): void {
        this.selectedAnswers = {};
        this.quizSubmissionStatus = {};
        this.currentQuizIndex = 0;
        this.showSummary = false;
        this.finalScore = 0;
    }

    calculateScore(): void {
        let totalScore = 0;
        this.quizzes.forEach((quiz, quizIndex) => {
            quiz.questions.forEach(question => {
                const studentAnswer = this.selectedAnswers[quizIndex][question.id];
                if (question.type === 'MULTIPLE_CHOICE' && (studentAnswer as string[]).includes(question.correctAnswer)) {
                    totalScore++;
                } else if ((question.type === 'SHORT_ANSWER' || question.type === 'LONG_ANSWER') &&
                    (studentAnswer as string).trim().toLowerCase() === question.correctAnswer.trim().toLowerCase()) {
                    totalScore++;
                }
            });
        });
        this.finalScore = totalScore;
        this.toastr.success(`Your final score: ${this.finalScore}`, 'Score');
    }



    nextQuiz(): void {
        if (this.currentQuizIndex < this.quizzes.length - 1) {
            if (this.validateQuizAnswers()) {
                this.currentQuizIndex++;
            } else {
                this.toastr.error('Please answer all questions before moving to the next quiz.', 'Validation Error');
            }
        } else {
            this.toastr.info('You have reached the last quiz', 'Info');
        }
    }

    previousQuiz(): void {
        if (this.currentQuizIndex > 0) {
            this.currentQuizIndex--;
        } else {
            this.toastr.info('You are already on the first quiz', 'Info');
        }
    }


    formatTime(seconds: number): string {
        const minutes: number = Math.floor(seconds / 60);
        const remainingSeconds: number = seconds % 60;
        return `${minutes} min ${remainingSeconds} sec`;
    }

    toggleOptionSelection(quizIndex: number, questionId: string, option: string): void {
        if (!this.selectedAnswers[quizIndex]) {
            this.selectedAnswers[quizIndex] = {};
        }
        if (!this.selectedAnswers[quizIndex][questionId]) {
            this.selectedAnswers[quizIndex][questionId] = [];  // Initialize as an array for multiple-choice
        }

        const selectedOptions = this.selectedAnswers[quizIndex][questionId] as string[];
        const index = selectedOptions.indexOf(option);
        if (index === -1) {
            selectedOptions.push(option);
        } else {
            selectedOptions.splice(index, 1);
        }
    }

// Submit short answer
    submitShortAnswer(quizIndex: number, questionId: string): void {
        if (!this.selectedAnswers[quizIndex]) {
            this.selectedAnswers[quizIndex] = {};
        }
        // No additional action required, as [(ngModel)] in the template handles updating the answer.
        this.toastr.success('Short answer submitted', 'Success');
    }

// Submit long answer
    submitLongAnswer(quizIndex: number, questionId: string): void {
        if (!this.selectedAnswers[quizIndex]) {
            this.selectedAnswers[quizIndex] = {};
        }
        // No additional action required, as [(ngModel)] in the template handles updating the answer.
        this.toastr.success('Long answer submitted', 'Success');
    }

}

