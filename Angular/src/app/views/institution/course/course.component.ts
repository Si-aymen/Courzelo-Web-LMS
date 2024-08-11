import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
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
      private courseService: CourseService
  ) { }
  courseID: string;
  user: UserResponse;
  course: CourseResponse;
  courseRequest: CourseRequest;
  loading = false;
    updateCourseForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
            description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
            credit: [0, [Validators.required]],
        }
    );
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
    leaveCourse(content: any) {
        this.modalService.open(content, { ariaLabelledBy: 'leave coutse' })
            .result.then((result) => {
            if (result === 'Ok') {
                this.courseService.leaveCourse(this.courseID).subscribe(
                    () => {
                        this.toastr.success('You have left the course');
                        this.authenticationService.refreshPageInfo();
                        this.router.navigateByUrl('dashboard/v1');
                    }, error => {
                        this.toastr.error('Error leaving course');
                    }
                );
            }
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    isUserTeacherInCourse(): boolean {
        return this.course.teacher === this.user.email;
    }
    shouldShowError(controlName: string, errorName: string): boolean {
        const control = this.updateCourseForm.get(controlName);
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

}
