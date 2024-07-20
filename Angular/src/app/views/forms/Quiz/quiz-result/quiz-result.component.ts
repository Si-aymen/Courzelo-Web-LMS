import {Component, Input, OnInit} from '@angular/core';
import {Quiz} from '../../../../shared/models/Quiz';

@Component({
  selector: 'app-quiz-result',
  templateUrl: './quiz-result.component.html',
  styleUrls: ['./quiz-result.component.scss']
})
export class QuizResultComponent implements OnInit {
  @Input() quizzes: Quiz[] = [];
  @Input() selectedAnswers: { [key: string]: string } = {};

  totalScore = 0;
  maxScore = 0;

  ngOnInit(): void {
    this.calculateScore();
  }

  calculateScore(): void {
    this.totalScore = 0;
    this.maxScore = 0;

    this.quizzes.forEach(quiz => {
      quiz.questions.forEach(question => {
        if (question.correctAnswer) {
          this.maxScore++;
          if (this.selectedAnswers[question.id] === question.correctAnswer) {
            this.totalScore++;
          }
        }
      });
    });
  }
}
