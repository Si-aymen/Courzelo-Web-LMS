import {Component, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SharedAnimations} from '../../../shared/animations/shared-animations';

@Component({
  selector: 'app-create-quiz',
  templateUrl: './create-quiz.component.html',
  styleUrls: ['./create-quiz.component.scss'],
  animations: [SharedAnimations]
})
export class CreateQuizComponent implements OnInit {
  quizForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.quizForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      questions: this.fb.array([this.createQuestion()])
    });
  }

  ngOnInit(): void {
        throw new Error('Method not implemented.');
    }

  get questionFormArray(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  createQuestion(): FormGroup {
    return this.fb.group({
      questionText: ['', Validators.required],
      options: this.fb.array([this.createOption(), this.createOption(), this.createOption(), this.createOption()]),
      correctAnswer: ['', Validators.required]
    });
  }

  createOption(): FormGroup {
    return this.fb.group({
      option: ['', Validators.required]
    });
  }

  addQuestion(): void {
    this.questionFormArray.push(this.createQuestion());
  }

  createQuiz(): void {
    if (this.quizForm.valid) {
      const quiz = this.quizForm.value;
      // Logic to create a quiz
      console.log(quiz);
    }
  }

}
