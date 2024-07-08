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
  currentQuestionIndex = 0;
  userAnswers: { questionId: string, answer: string }[] = [];
  quizId: string;

  constructor(private quizService: QuizService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.quizId = this.route.snapshot.paramMap.get('id');
    this.loadQuiz();
  }

  loadQuiz(): void {
    this.quizService.getQuizById(this.quizId).subscribe(
        response => {
          this.quiz = response;
        },
        error => {
          console.error('Error loading quiz:', error);
        }
    );
  }

  submitAnswer(questionId: string, answer: string): void {
    const answerIndex = this.userAnswers.findIndex(ans => ans.questionId === questionId);
    if (answerIndex > -1) {
      this.userAnswers[answerIndex].answer = answer;
    } else {
      this.userAnswers.push({ questionId, answer });
    }
  }

  nextQuestion(): void {
    if (this.currentQuestionIndex < this.quiz.questions.length - 1) {
      this.currentQuestionIndex++;
    } else {
      this.submitQuiz();
    }
  }

  previousQuestion(): void {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
    }
  }

  submitQuiz(): void {
    const correctAnswers = this.quiz.questions.map(q => ({
      questionId: q.id,
      correctAnswer: q.correctAnswer
    }));

    const results = this.userAnswers.map(userAnswer => {
      const correctAnswer = correctAnswers.find(ca => ca.questionId === userAnswer.questionId);
      return {
        questionId: userAnswer.questionId,
        isCorrect: userAnswer.answer === correctAnswer?.correctAnswer
      };
    });

    // Optionally, you can display results to the user here
    console.log('Quiz results:', results);

    // Send results to the server or handle as needed
    this.quizService.submitQuiz(this.quizId, results).subscribe(
        response => {
          console.log('Quiz submitted:', response);
          this.quizService.toastr.success('Quiz submitted successfully', 'Success');
        },
        error => {
          console.error('Error submitting quiz:', error);
        }
    );
  }

}
