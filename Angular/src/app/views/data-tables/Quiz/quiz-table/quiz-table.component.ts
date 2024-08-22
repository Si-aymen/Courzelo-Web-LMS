import {Component, OnInit} from '@angular/core';
import {DataLayerService} from '../../../../shared/services/data-layer.service';
import {QuizService} from '../../../../shared/services/quiz.service';
import {Quiz} from '../../../../shared/models/Quiz';
import {ToastrService} from 'ngx-toastr';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {EditQuizComponent} from '../../../forms/Quiz/edit-quiz/edit-quiz.component';
import {Routes} from '@angular/router';

@Component({
  selector: 'app-quiz-table',
  templateUrl: './quiz-table.component.html',
  styleUrls: ['./quiz-table.component.scss'],
  animations: [
    trigger('animate', [
      state('in', style({transform: 'translateX(0)'})),
      transition('void => *', [
        style({transform: 'translateX(-100%)'}),
        animate(100)
      ]),
      transition('* => void', [
        animate(100, style({transform: 'translateX(100%)'}))
      ])])]})
export class QuizTableComponent implements OnInit {
  viewMode: 'list' | 'grid' = 'list';
  allSelected = false;
  page = 1;
  pageSize = 8;
  quizzes: Quiz[] = [];
  selectedQuiz: Quiz | null = null; // Stores the quiz to be edited

  constructor(private quizService: QuizService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadQuizzes();
  }

  loadQuizzes(): void {
    this.quizService.getAllQuizzes().subscribe(
        (quizzes: Quiz[]) => {
          this.quizzes = quizzes;
        },
        error => {
          console.error('Error fetching quizzes', error);
        }
    );
  }

  selectAll(e: Event): void {
    this.quizzes = this.quizzes.map(quiz => {
      quiz.isSelected = this.allSelected;
      return quiz;
    });
  }

  selectQuizToEdit(quiz: Quiz): void {
    this.selectedQuiz = { ...quiz };
  }

  onQuizUpdated(updatedQuiz: Quiz): void {
    const index = this.quizzes.findIndex(q => q.id === updatedQuiz.id);
    if (index > -1) {
      this.quizzes[index] = updatedQuiz;
    }
    this.selectedQuiz = null; // Clear selection after update
  }

  deleteQuiz(id: string): void {
    this.quizService.deleteQuiz(id).subscribe(
        () => {
          this.toastr.success('Quiz deleted successfully', 'Success');
          this.loadQuizzes();
        },
        error => {
          console.error('Error deleting quiz', error);
          this.toastr.error('Failed to delete quiz', 'Error');
        }
    );
  }

  deleteSelectedQuizzes(): void {
    const selectedQuizzes = this.quizzes.filter(quiz => quiz.isSelected);
    const deleteRequests = selectedQuizzes.map(quiz => this.quizService.deleteQuiz(quiz.id).toPromise());

    Promise.all(deleteRequests)
        .then(() => {
          this.toastr.success('Selected quizzes deleted successfully', 'Success');
          this.loadQuizzes(); // Refresh quiz list after deletion
        })
        .catch(error => {
          console.error('Error deleting selected quizzes', error);
          this.toastr.error('Failed to delete selected quizzes', 'Error');
        });
  }

  closeEdit(): void {
    this.selectedQuiz = null; // Close the edit form without saving
  }
}
