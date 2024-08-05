import {Component, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {DomSanitizer} from '@angular/platform-browser';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder} from '@angular/forms';
import {CourseService} from '../../../shared/services/institution/course.service';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {CourseResponse} from '../../../shared/models/institution/CourseResponse';
import {InstitutionUserResponse} from '../../../shared/models/institution/InstitutionUserResponse';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {Subscription} from "rxjs";

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
      private sanitizer: DomSanitizer,
      private sessionstorage: SessionStorageService,
      private modalService: NgbModal,
      private formBuilder: FormBuilder,
      private courseService: CourseService
  ) { }
  courseID: string;
  user: UserResponse;
  course: CourseResponse;
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
        this.modalService.open(content, { ariaLabelledBy: 'confirm User' })
            .result.then((result) => {
            if (result === 'Ok') {
                this.courseService.deleteCourse(this.courseID).subscribe(
                    () => {
                        this.toastr.success('Course deleted successfully');
                        this.authenticationService.refreshPageInfo();
                    }, error => {
                        this.toastr.error('Error deleting course');
                    }
                );
            }
        }, (reason) => {
            console.log('Err!', reason);
        });
    }

}
