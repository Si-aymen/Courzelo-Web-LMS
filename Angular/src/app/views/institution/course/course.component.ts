import {Component, OnInit} from '@angular/core';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {ActivatedRoute} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {DomSanitizer} from '@angular/platform-browser';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder} from '@angular/forms';
import {CourseService} from '../../../shared/services/institution/course.service';
import {UserResponse} from "../../../shared/models/user/UserResponse";

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.scss']
})
export class CourseComponent implements OnInit {
  constructor(
      private institutionService: InstitutionService,
      private route: ActivatedRoute,
      private toastr: ToastrService,
      private sanitizer: DomSanitizer,
      private sessionstorage: SessionStorageService,
      private modalService: NgbModal,
      private formBuilder: FormBuilder,
      private courseService: CourseService
  ) { }
  courseID: string;
  user: UserResponse;
  imageSrc: any;
    ngOnInit(): void {
      this.courseID = this.route.snapshot.paramMap.get('courseID');
      this.sessionstorage.getUser().subscribe(
            user => {
              this.user = user;
            }
            );
  }

}
