import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {DomSanitizer} from '@angular/platform-browser';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, Validators} from '@angular/forms';
import {CourseService} from '../../../shared/services/institution/course.service';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {CourseResponse} from '../../../shared/models/institution/CourseResponse';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {Subscription} from 'rxjs';
import {CourseRequest} from '../../../shared/models/institution/CourseRequest';
import {UserService} from '../../../shared/services/user/user.service';
import {CoursePostRequest} from '../../../shared/models/institution/CoursePostRequest';
import {QuestionType} from '../../../shared/models/QuestionType';
import {Quiz} from '../../../shared/models/Quiz';
import {status} from '../../../shared/models/status';
import {Question} from '../../../shared/models/Question';
import {QuizService} from '../../../shared/services/quiz.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.scss']
})
export class CourseComponent implements OnInit, OnDestroy {
  constructor(
      private authenticationService: AuthenticationService,
      private route: ActivatedRoute,
      private router: Router,
      private toastr: ToastrService,
      private sessionstorage: SessionStorageService,
      private modalService: NgbModal,
      private formBuilder: FormBuilder,
      private courseService: CourseService,
      private userService: UserService,
      private sanitizer: DomSanitizer,
        private quizService: QuizService
  ) { }
    quizToAdd: Quiz = {
        status: status.COMPLETED, // Corrected property name from `Status` to `status`
        category: '',
        isSelected: false,
        id: '',
        userEmail: '',
        title: '',
        description: '',
        questions: [],
        duration: 0,
        maxAttempts: 0,
        score: 0,
        course: null
    };
    selectedAnswers: { [quizID: string]: { [questionId: string]: string[] | string } } = {};
    quizSubmissionStatus: { [key: string]: boolean } = {};
  courseID: string;
  user: UserResponse;
  course: CourseResponse;
  courseRequest: CourseRequest;
  postRequest: CoursePostRequest = {} as CoursePostRequest;
  files: File[] = [];
  loading = false;
    updateCourseForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
            description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
            credit: [0, [Validators.required]],
        }
    );
    addPostForm = this.formBuilder.group({
            title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
            description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    });
    private routeSub: Subscription;
    imageSrc: any;

    protected readonly QuestionType = QuestionType;
    statuses = Object.values(status); // List of all possible statuses
    ngOnInit(): void {
      this.sessionstorage.getUser().subscribe(
            user => {
              this.user = user;
            }
            );
        this.routeSub = this.route.params.subscribe(params => {
            this.courseID = params['courseID'];
            this.fetchCourse();
        });
        if (this.courseID == null || this.courseID === '') {
            this.toastr.error('Course not found');
            this.router.navigateByUrl('dashboard/v1');
        }
  }
    ngOnDestroy(): void {
        if (this.routeSub) {
            this.routeSub.unsubscribe();
        }
    }
    // Submit short answer
    submitShortAnswer(quizIndex: number): void {
        if (!this.selectedAnswers[quizIndex]) {
            this.selectedAnswers[quizIndex] = {};
        }
        // No additional action required, as [(ngModel)] in the template handles updating the answer.
        this.toastr.success('Short answer submitted', 'Success');
    }
    validateQuizAnswers(quiz: Quiz): boolean {
        return quiz.questions.every(question => {
            const answer = this.selectedAnswers[quiz.id]?.[question.id];
            console.log('Answer:', answer);
            if (question.type === 'MULTIPLE_CHOICE') {
                return Array.isArray(answer) && answer.length > 0;
            } else {
                return typeof answer === 'string' && answer.trim().length > 0;
            }
        });
    }
    submitQuiz(quiz: Quiz): void {
        if (!this.validateQuizAnswers(quiz)) {
            this.toastr.error('Please answer all questions before submitting the quiz.', 'Validation Error');
            return;
        }

        const currentQuiz = quiz;
        if (currentQuiz && currentQuiz.questions) {
            console.log('Submitting quiz:', currentQuiz);
            console.log('Selected answers:', this.selectedAnswers);
            this.quizSubmissionStatus[currentQuiz.id] = true;
            this.toastr.success('Quiz submitted successfully', 'Success');

         /*   if (this.currentQuizIndex === this.quizzes.length - 1) {
                this.showSummary = true;
                this.calculateScore();
            } else {
                this.currentQuizIndex++;
            }*/
        }
    }

// Submit long answer
    submitLongAnswer(quizIndex: number, questionId: string): void {
        if (!this.selectedAnswers[quizIndex]) {
            this.selectedAnswers[quizIndex] = {};
        }
        // No additional action required, as [(ngModel)] in the template handles updating the answer.
        this.toastr.success('Long answer submitted', 'Success');
    }
    toggleOptionSelection(quizID: string, questionId: string, option: string): void {
        if (!this.selectedAnswers[quizID]) {
            this.selectedAnswers[quizID] = {};
        }
        if (!this.selectedAnswers[quizID][questionId]) {
            this.selectedAnswers[quizID][questionId] = [];  // Initialize as an array for multiple-choice
        }

        const selectedOptions = this.selectedAnswers[quizID][questionId] as string[];
        const index = selectedOptions.indexOf(option);
        if (index === -1) {
            selectedOptions.push(option);
        } else {
            selectedOptions.splice(index, 1);
        }
    }
    trackByIndex(index: number, obj: any): any {
        return index;
    }
    addQuestion(): void {
        const newQuestion: Question = {
            id: '',
            text: '',
            options: [''],
            correctAnswer: '',
            type: QuestionType.MULTIPLE_CHOICE,
            answers: [],
        };
        this.quizToAdd.questions.push(newQuestion);
    }

    addOption(questionIndex: number): void {
        this.quizToAdd.questions[questionIndex].options.push('');
    }

    removeOption(questionIndex: number, optionIndex: number): void {
        this.quizToAdd.questions[questionIndex].options.splice(optionIndex, 1);
    }
    addQuiz(): void {
        this.quizToAdd.course = this.courseID;
        console.log('Quiz status before saving:', this.quizToAdd.status);
        this.quizService.saveQuiz(this.quizToAdd).subscribe(
            response => {
                console.log('Quiz created:', response);
                console.log('Quiz status after saving:', this.quizToAdd.status);
                this.quizService.toastr.success('Quiz submitted successfully', 'Success');
            },
            error => {
                console.error('Error creating quiz:', error);
            }
        );
    }
    addQuizModel(content) {
        this.modalService.open( content, { ariaLabelledBy: 'add Quiz' })
            .result.then((result) => {
            console.log(result);
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    fetchCourse() {
        this.courseService.getCourse(this.courseID).subscribe(
            course => {
                this.course = course;
                console.log(this.course);
                if (this.course.teacher) {
                    this.userService.getProfileImageBlobUrl(course.teacher).subscribe((blob: Blob) => {
                        const objectURL = URL.createObjectURL(blob);
                        this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
                    });
                }
            }, error => {
                console.error('Error fetching course:', error);
                this.toastr.error(error.error);
                this.router.navigateByUrl('dashboard/v1');
            }
        );
    }
    deleteCourse(content: any) {
        this.modalService.open(content, { ariaLabelledBy: 'delete course' })
            .result.then((result) => {
            if (result === 'Ok') {
                this.courseService.deleteCourse(this.courseID).subscribe(
                    () => {
                        this.toastr.success('Course deleted successfully');
                        this.authenticationService.refreshPageInfo();
                        this.router.navigateByUrl('dashboard/v1');
                    }, error => {
                        this.toastr.error('Error deleting course');
                    }
                );
            }
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    deletePostModal(content: any, postID: string) {
        this.modalService.open(content, { ariaLabelledBy: 'delete post' })
            .result.then((result) => {
            if (result === 'Ok') {
                this.deletePost(postID);
            }
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    isUserTeacherInCourse(): boolean {
        return this.course.teacher === this.user.email;
    }
    shouldShowErrorUpdateCourse(controlName: string, errorName: string): boolean {
        const control = this.updateCourseForm.get(controlName);
        return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
    }
    shouldShowErrorAddPost(controlName: string, errorName: string): boolean {
        const control = this.addPostForm.get(controlName);
        return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
    }
    updateCourseModel(content) {
        this.updateCourseForm.patchValue({
            name: this.course.name,
            description: this.course.description,
            credit: this.course.credit
        });
        this.modalService.open( content, { ariaLabelledBy: 'Update Course' })
            .result.then((result) => {
            console.log(result);
        }, (reason) => {
            console.log('Err!', reason);
        });
    }

    updateCourse() {
        this.loading = true;
        if (this.updateCourseForm.valid) {
            this.courseRequest = this.updateCourseForm.getRawValue();
            this.courseService.updateCourse(this.courseID, this.courseRequest).subscribe(
                () => {
                    this.toastr.success('Course updated successfully');
                    this.fetchCourse();
                    this.loading = false;
                }, error => {
                    console.error('Error updating course:', error);
                    this.toastr.error('Error updating course');
                    this.loading = false;
                }
            );
        } else {
            this.toastr.error('Please fill all fields correctly');
            this.loading = false;
        }
    }
    addPostModel(content) {
        this.modalService.open( content, { ariaLabelledBy: 'add Post' })
            .result.then((result) => {
            console.log(result);
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    addPost() {
        this.loading = true;
        if (this.addPostForm.valid) {
            this.postRequest.title = this.addPostForm.controls['title'].value;
            this.postRequest.description = this.addPostForm.controls['description'].value;
            console.log(this.postRequest);
            this.courseService.addPost(this.courseID, this.postRequest, this.files).subscribe(
                () => {
                    this.toastr.success('Post added successfully');
                    this.fetchCourse();
                    this.loading = false;
                    this.addPostForm.reset();
                    this.files = [];
                }, error => {
                    console.error('Error adding post:', error);
                    this.toastr.error('Error adding post');
                    this.loading = false;
                    this.files = [];
                }
            );
        } else {
            this.toastr.error('Please fill all fields correctly');
            this.loading = false;
        }
    }
    downloadFile(fileName: string) {
        this.courseService.downloadFile(this.courseID, fileName).subscribe(
            response => {
                const blob = new Blob([response]);

                // Create a link element
                const link = document.createElement('a');

                // Set the download attribute with the filename
                link.href = window.URL.createObjectURL(blob);
                link.download = fileName;

                // Append the link to the body
                document.body.appendChild(link);

                // Programmatically click the link to trigger the download
                link.click();

                // Clean up by removing the link from the document
                document.body.removeChild(link);

                // Revoke the object URL to release memory
                window.URL.revokeObjectURL(link.href);
            }, error => {
                console.error('Error downloading file:', error);
                this.toastr.error('Error downloading file');
            });
    }

    onFileSelected(event) {
        this.files = [];
        if (event.target.files.length > 0) {
            for (let i = 0; i < event.target.files.length; i++) {
                this.files.push(event.target.files[i]);
            }
        }
    }
    deletePost(postID: string) {
        this.courseService.deletePost(this.courseID, postID).subscribe(
            () => {
                this.toastr.success('Post deleted successfully');
                this.fetchCourse();
            }, error => {
                console.error('Error deleting post:', error);
                this.toastr.error('Error deleting post');
            }
        );
    }
}
