import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {QuizService} from '../../../../shared/services/quiz.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Quiz} from '../../../../shared/models/Quiz';

const output = Output();

@Component({
  selector: 'app-edit-quiz',
  templateUrl: './edit-quiz.component.html',
  styleUrls: ['./edit-quiz.component.scss']
})
export class EditQuizComponent implements OnInit {
    @Input() quiz: Quiz | null = null; // Receives the quiz to edit
    @Output() quizUpdated = new EventEmitter<Quiz>(); // Emits the updated quiz
    statuses: string[] = ['DONE', 'IN_PROGRESS', 'NOT_SUBMITTED', 'PASSED', 'COMPLETED', 'FAILED', 'PENDING'];

    constructor(
        private quizService: QuizService,
        private route: ActivatedRoute,
        private router: Router,
        private toastr: ToastrService
    ) {}

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            const quizId = params['id'];
            if (quizId) {
                this.loadQuiz(quizId);
            } else {
                console.error('Quiz ID is not available');
                this.toastr.error('Quiz ID is not available', 'Error');
            }
        });
    }

    loadQuiz(id: string): void {
        this.quizService.getQuizById(id).subscribe(
            (quiz: Quiz) => {
                this.quiz = quiz;
            },
            error => {
                console.error('Error loading quiz:', error);
                this.toastr.error('Failed to load quiz', 'Error');
            }
        );
    }

    updateQuiz(): void {
        if (this.quiz && this.quiz.id) {
            this.quizService.updateQuiz(this.quiz.id, this.quiz).subscribe(
                response => {
                    this.toastr.success('Quiz updated successfully', 'Success');
                    // Optionally navigate after successful update
                },
                error => {
                    console.error('Error updating quiz:', error);
                    this.toastr.error('Failed to update quiz', 'Error');
                }
            );
        } else {
            console.error('Quiz ID is missing');
            this.toastr.error('Quiz ID is missing', 'Error');
        }
    }
}
