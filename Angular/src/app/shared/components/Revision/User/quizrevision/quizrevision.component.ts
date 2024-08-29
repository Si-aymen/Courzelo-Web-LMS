import { Component, OnInit } from '@angular/core';

import {DomSanitizer} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import { AnswerRevision } from 'src/app/shared/models/Revision/QuizzRevisison/AnswerRevision';
import { QuestionRevision } from 'src/app/shared/models/Revision/QuizzRevisison/QuestionRevision';
import { QuizRevision } from 'src/app/shared/models/Revision/QuizzRevisison/QuizRevision ';
import { LoginResponse } from 'src/app/shared/models/user/LoginResponse';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { RevisionService } from 'src/app/shared/services/Revision/revision.service';
import { ResponseHandlerService } from 'src/app/shared/services/user/response-handler.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
  selector: 'app-quizrevision',
  templateUrl: './quizrevision.component.html',
  styleUrls: ['./quizrevision.component.scss']
})
export class QuizrevisionComponent implements OnInit {

  imageSrc: any;
  user: UserResponse;
  questions: QuestionRevision[] = [];
  quizRevisionId: string = '';
  quiz: QuizRevision;

  constructor(
      private sessionStorageService: SessionStorageService,
      private userService: UserService,
      private sanitizer: DomSanitizer,
      private route: ActivatedRoute,
      private router: Router,
      private HandleResponse: ResponseHandlerService,
      private revisionService: RevisionService
  ) { }

  ngOnInit() {
      this.route.paramMap.subscribe(params => {
          let userEmail = params.get('email');
          //if (!userEmail) {
             //   userEmail = this.sessionStorageService.getUser();
        //  }
          this.userService.getUserProfileByEmail(userEmail).subscribe((user: LoginResponse) => {
                this.user = user.user;
                this.userService.getProfileImageBlobUrl(this.user.email).subscribe((blob: Blob) => {
                    const objectURL = URL.createObjectURL(blob);
                    this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
                });
          }, error => {
              this.HandleResponse.handleError(error);
              this.router.navigateByUrl('/others/404');
          });
      });

      this.route.paramMap.subscribe(params => {
        this.quizRevisionId = params.get('id')!;
  
        if (this.quizRevisionId) {
          this.loadQuestions();
        }
      });
  }

  loadQuestions() {
    this.revisionService.getQuestionsByQuizRevisionId(this.quizRevisionId).subscribe(
      (questions: QuestionRevision[]) => {
        // Initialize answerSubmitted for each question
        this.questions = questions.map(question => ({
            ...question,
            answerSubmitted: false // Initialize answerSubmitted
        }));
      },
      (error) => {
        console.error('Error loading questions:', error);
      }
    );
  }
  submitAnswer(question: QuestionRevision) {
    const answerSubmission = {
      questionId: question.id,
      userAnswerText: question.userAnswerText  // Ensure this key matches the backend parameter
    };

    console.log('Answer Submission:', answerSubmission);  // Debugging: Log the submission data

    this.revisionService.submitAnswer(answerSubmission).subscribe(isCorrect => {
      question.answerSubmitted = true;
      question.isCorrect = isCorrect;
      // Optionally, you can set the correct answer here if you want to display it
      question.correctAnswer = question.correctAnswer; 
    });
}
}