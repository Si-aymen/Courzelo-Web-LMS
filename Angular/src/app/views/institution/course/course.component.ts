import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
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
import {CoursePostResponse} from '../../../shared/models/institution/CoursePostResponse';

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
      private sanitizer: DomSanitizer
  ) { }
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
    fetchCourse() {
        this.courseService.getCourse(this.courseID).subscribe(
            course => {
                this.course = course;
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
