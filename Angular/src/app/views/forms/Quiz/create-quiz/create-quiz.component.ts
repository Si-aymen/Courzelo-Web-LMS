import {Component} from '@angular/core';
import {SharedAnimations} from '../../../../shared/animations/shared-animations';
import {QuizService} from '../../../../shared/services/quiz.service';
import {Question} from '../../../../shared/models/Question';
import {Quiz} from '../../../../shared/models/Quiz';
import {QuestionType} from '../../../../shared/models/QuestionType';
import {status} from '../../../../shared/models/status';

@Component({
  selector: 'app-create-quiz',
  templateUrl: './create-quiz.component.html',
  styleUrls: ['./create-quiz.component.scss'],
  animations: [SharedAnimations]
})
export class CreateQuizComponent  {
  selectedAnswer: string;
  quiz: Quiz = {
    status: status.COMPLETED, // Corrected property name from `Status` to `status`
    category: '',
    isSelected: false,
    id: '',
    title: '',
    description: '',
    questions: [],
    duration: 0,
    maxAttempts: 0,
    score: 0
  };

  statuses = Object.values(status); // List of all possible statuses

  constructor(private quizService: QuizService) {}

  addQuestion(): void {
    const newQuestion: Question = {
      id: '',
      text: '',
      options: [''],
      correctAnswer: '',
      type: QuestionType.MULTIPLE_CHOICE,
      answers: [],
    };
    this.quiz.questions.push(newQuestion);
  }

  addOption(questionIndex: number): void {
    this.quiz.questions[questionIndex].options.push('');
  }

  removeOption(questionIndex: number, optionIndex: number): void {
    this.quiz.questions[questionIndex].options.splice(optionIndex, 1);
  }

  submitQuiz(): void {
    console.log('Quiz status before saving:', this.quiz.status);
    this.quizService.saveQuiz(this.quiz).subscribe(
        response => {
          console.log('Quiz created:', response);
          console.log('Quiz status after saving:', this.quiz.status);
          this.quizService.toastr.success('Quiz submitted successfully', 'Success');
        },
        error => {
          console.error('Error creating quiz:', error);
        }
    );
  }

  trackByIndex(index: number, obj: any): any {
    return index;
  }

  protected readonly QuestionType = QuestionType;
}
