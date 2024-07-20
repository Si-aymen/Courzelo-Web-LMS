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
    selectedAnswers: { [key: string]: string[] } = {}; // Object to store selected answers per question
    quizSubmissionStatus: { [key: string]: boolean } = {}; // Object to track submission status of each quiz
    showSummary = false; // Flag to display summary
    remainingTime: number; // Remaining time for the current quiz in seconds
    quizTimer: any; // Timer reference
    finalScore = 0;

    constructor(
        private quizService: QuizService,
        private toastr: ToastrService,
        private route: ActivatedRoute
    ) {}

    ngOnInit(): void {
        // Fetch all quizzes initially
        this.fetchAllQuizzes();
    }
    fetchAllQuizzes(): void {
        console.log('Fetching all quizzes...');
        this.quizService.getAllQuizzes().subscribe(
            (quizzes: Quiz[]) => {
                if (quizzes && quizzes.length > 0) {
                    this.quizzes = quizzes;
                    console.log('Quizzes fetched:', this.quizzes);
                    this.quizzes.forEach(quiz => {
                        quiz.questions.forEach(question => {
                            this.selectedAnswers[question.id] = []; // Initialize selected answer as an empty array
                        });
                        this.quizSubmissionStatus[quiz.id] = false;
                    });
                    // Start timer for the first quiz
                    this.startQuizTimer();
                } else {
                    console.error('No quizzes found');
                    this.toastr.error('No quizzes found', 'Error');
                }
            },
            error => {
                console.error('Error fetching quizzes', error);
                this.toastr.error('Failed to load quizzes', 'Error');
            }
        );
    }

    startQuizTimer(): void {
        if (this.quizzes.length > 0) {
            this.remainingTime = this.quizzes[this.currentQuizIndex].duration * 60; // Convert minutes to seconds
            this.quizTimer = setInterval(() => {
                if (this.remainingTime > 0) {
                    this.remainingTime--;
                } else {
                    clearInterval(this.quizTimer);
                    this.toastr.info('Time is up!', 'Info');
                    this.submitQuiz();
                }
            }, 1000); // Update every second
        }
    }

    clearQuizTimer(): void {
        clearInterval(this.quizTimer); // Clear the quiz timer
    }

    /*selectMultipleChoiceAnswer(questionId: string, answer: string): void {
        console.log(`Selecting answer for multiple choice question ID ${questionId}: ${answer}`);
        this.selectedAnswers[questionId] = answer;
    }*/
    selectMultipleChoiceAnswer(questionId: string, selectedOption: any) {
        this.selectedAnswers[questionId] = selectedOption;
    }


    submitShortAnswer(questionId: string): void {
        console.log(`Submitting short answer for question ID ${questionId}: ${this.selectedAnswers[questionId]}`);

        // Optionally, you can perform additional actions like saving to backend here

        this.toastr.success('Short answer submitted successfully', 'Success');
    }

    submitLongAnswer(questionId: string): void {
        console.log(`Submitting long answer for question ID ${questionId}: ${this.selectedAnswers[questionId]}`);

        // Optionally, you can perform additional actions like saving to backend here

        this.toastr.success('Long answer submitted successfully', 'Success');
    }

    submitQuiz(): void {
        const currentQuiz = this.quizzes[this.currentQuizIndex];
        if (currentQuiz && currentQuiz.questions) {
            // Submit answers for the current quiz
            console.log('Submitting quiz:', currentQuiz);
            console.log('Selected answers:', this.selectedAnswers);

            // Mark quiz as submitted
            this.quizSubmissionStatus[currentQuiz.id] = true;
            this.toastr.success('Quiz submitted successfully', 'Success');

            // If it's the last quiz, show summary
            if (this.currentQuizIndex === this.quizzes.length - 1) {
                this.showSummary = true;
                this.clearQuizTimer(); // Stop the timer if it's the last quiz
            } else {
                // Move to the next quiz and start timer
                this.currentQuizIndex++;
                this.startQuizTimer();
            }
        }
        this.showSummary = true;
        this.calculateScore();
    }
    loadQuizzes(): void {
        // Load quizzes from your data source
        // Example:
        this.quizzes = [ /* Your quizzes data here */ ];
    }
    resetQuiz(): void {
        this.selectedAnswers = {}; // Reset selected answers
        this.quizSubmissionStatus = {}; // Reset submission status
        this.currentQuizIndex = 0; // Reset to the first quiz
        this.showSummary = false; // Reset showSummary flag
        this.clearQuizTimer(); // Stop the timer
        this.startQuizTimer(); // Start timer for the first quiz
        this.finalScore = 0;
    }

    calculateScore(): void {
        let score = 0;
        this.quizzes.forEach(quiz => {
            quiz.questions.forEach(question => {
                if (question.type === 'MULTIPLE_CHOICE' && this.selectedAnswers[question.id].includes(question.correctAnswer)) {
                    score++;
                } else if (question.type === 'SHORT_ANSWER' && this.selectedAnswers[question.id].includes(question.correctAnswer)) {
                    score++;
                } else if (question.type === 'LONG_ANSWER' && this.selectedAnswers[question.id].includes(question.correctAnswer)) {
                    score++;
                }
            });
        });
        this.quizzes[this.currentQuizIndex].score = score;
        this.finalScore = score; // Add this line to assign the final score
    }
    nextQuiz(): void {
        if (this.currentQuizIndex < this.quizzes.length - 1) {
            this.currentQuizIndex++;
            console.log('Moving to next quiz:', this.currentQuizIndex);
            this.clearQuizTimer(); // Stop current timer
            this.startQuizTimer(); // Start timer for the next quiz
        } else {
            this.toastr.info('You have reached the last quiz', 'Info');
        }
    }

    previousQuiz(): void {
        if (this.currentQuizIndex > 0) {
            this.currentQuizIndex--;
            console.log('Moving to previous quiz:', this.currentQuizIndex);
            this.clearQuizTimer(); // Stop current timer
            this.startQuizTimer(); // Start timer for the previous quiz
        } else {
            this.toastr.info('You are already on the first quiz', 'Info');
        }
    }

    formatTime(seconds: number): string {
        const minutes: number = Math.floor(seconds / 60);
        const remainingSeconds: number = seconds % 60;
        return `${minutes} min ${remainingSeconds} sec`;
    }

    toggleOptionSelection(questionId: string, option: string) {
        if (!this.selectedAnswers[questionId]) {
            this.selectedAnswers[questionId] = []; // Initialize if not exists
        }

        const index = this.selectedAnswers[questionId].indexOf(option);
        if (index === -1) {
            this.selectedAnswers[questionId].push(option); // Add option if not already selected
        } else {
            this.selectedAnswers[questionId].splice(index, 1); // Remove option if already selected
        }
    }

}

