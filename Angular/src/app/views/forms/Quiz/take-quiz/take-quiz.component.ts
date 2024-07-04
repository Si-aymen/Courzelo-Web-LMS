import {Component, OnInit} from '@angular/core';
import {Quiz} from '../../../../shared/models/Quiz';
import {ActivatedRoute} from '@angular/router';
import {QuizService} from '../../../../shared/services/quiz.service';
import {SharedAnimations} from '../../../../shared/animations/shared-animations';

@Component({
  selector: 'app-take-quiz',
  templateUrl: './take-quiz.component.html',
  styleUrls: ['./take-quiz.component.scss'],
  animations: [SharedAnimations]
})
export class TakeQuizComponent implements OnInit {
  quiz: Quiz;
  answers: string[] = [];
  quizSubmitted = false;
  score = 0;

  constructor(
      private quizService: QuizService
  ) {}

  ngOnInit(): void {
    this.quiz = new Quiz();
  }

  submitQuiz(): void {
    this.score = this.quizService.calculateScore(this.quiz.id, this.answers);
    this.quizSubmitted = true;
  }

}
