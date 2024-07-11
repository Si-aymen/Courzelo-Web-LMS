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
    selectedAnswers: { [key: string]: string } = {}; // Object to store selected answers per question
    quizSubmissionStatus: { [key: string]: boolean } = {}; // Object to track submission status of each quiz
    showSummary = false; // Flag to display summary
    private answer: any;

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
                            this.selectedAnswers[question.id] = ''; // Initialize selected answer as an empty string
                        });
                        this.quizSubmissionStatus[quiz.id] = false;
                    });
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


    selectAnswer(questionId: string): void {
        console.log(`Selecting answer for question ID ${questionId}: ${(this.answer)}`);
        this.selectedAnswers[questionId] = this.answer;
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
            }
        }
    }

    nextQuiz(): void {
        if (this.currentQuizIndex < this.quizzes.length - 1) {
            this.currentQuizIndex++;
            console.log('Moving to next quiz:', this.currentQuizIndex);
        } else {
            this.toastr.info('You have reached the last quiz', 'Info');
        }
    }
    previousQuiz(): void {
        if (this.currentQuizIndex > 0) {
            this.currentQuizIndex--;
            console.log('Moving to previous quiz:', this.currentQuizIndex);
        } else {
            this.toastr.info('You are already on the first quiz', 'Info');
        }
    }

    resetQuiz(): void {
        this.selectedAnswers = {}; // Reset selected answers
        this.quizSubmissionStatus = {}; // Reset submission status
        this.currentQuizIndex = 0; // Reset to the first quiz
    }

    trackByIndex(index: number, obj: any): any {
        return index;
    }
}
