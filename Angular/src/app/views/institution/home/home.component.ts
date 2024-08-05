import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {InstitutionResponse} from '../../../shared/models/institution/InstitutionResponse';
import * as L from 'leaflet';
import {ToastrService} from 'ngx-toastr';
import {DomSanitizer} from '@angular/platform-browser';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, Validators} from '@angular/forms';
import {CourseService} from '../../../shared/services/institution/course.service';
import {CourseRequest} from '../../../shared/models/institution/CourseRequest';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
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
  institutionID: string;
  imageSrc: any;
  loading = false;
    code: string;
  currentInstitution: InstitutionResponse;
  course: CourseRequest = {} as CourseRequest;
  currentUser = this.sessionstorage.getUserFromSession();
  private map: L.Map | undefined;
  private marker: L.Marker | undefined;
    createCourseForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
            description: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
            credit: [0, [Validators.required]],
        }
    );
  ngOnInit() {
    this.institutionID = this.route.snapshot.paramMap.get('institutionID');
      this.route.queryParams.subscribe(params => {
          this.code = params['code'];
      });
    this.institutionService.getInstitutionByID(this.institutionID).subscribe(
        response => {
          this.currentInstitution = response;
            if (this.code && this.currentInstitution) {
                this.institutionService.acceptInvite(this.code).subscribe(
                    res => {
                        this.toastr.success('You have successfully joined ' + this.currentInstitution.name);
                    },
                    error => {
                        console.error(error);
                        this.toastr.error('Error accepting invitation');
                    }
                );
            } else {
                if (!this.code) {
                    console.log('No code');
                }
                if (!this.currentInstitution) {
                    console.log('No currentInstitution');
                }
            }
          console.log('currentInstitution', this.currentInstitution);
        }
    );
      this.institutionService.getImageBlobUrl(this.institutionID).subscribe((blob: Blob) => {
          const objectURL = URL.createObjectURL(blob);
          this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
      });

  }
  onTabChange(event: any) {
    if (event.nextId === 'mapTab') {
      setTimeout(() => {
        this.initializeMap();
      }, 0);
    }
  }
    addCourse() {
        this.loading = true;
        if (this.createCourseForm.valid) {
            this.course = this.createCourseForm.getRawValue();
            this.courseService.addCourse(this.institutionID, this.course).subscribe(
                response => {
                    this.toastr.success('Course added successfully');
                    this.createCourseForm.reset();
                    this.loading = false;
                },
                error => {
                    console.error(error);
                    this.toastr.error('Error adding course');
                    this.loading = false;
                }
            );
        } else {
            console.log(this.createCourseForm.errors);
            this.toastr.error('Please fill all fields correctly');
            this.loading = false;
        }
    }
    shouldShowError(controlName: string, errorName: string): boolean {
        const control = this.createCourseForm.get(controlName);
        return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
    }
    addCourseModel(content) {
        this.modalService.open( content, { ariaLabelledBy: 'Create Course' })
            .result.then((result) => {
            console.log(result);
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
  downloadExcel() {
    this.institutionService.downloadExcel(this.institutionID).subscribe(
        response => {
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = 'file.xlsx';
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error => {
          console.log('error downloading');
          this.toastr.error('Error downloading Excel.');
        }
    );
  }
  initializeMap() {
    if (this.currentInstitution.latitude === 0 || this.currentInstitution.longitude === 0 ||
        this.currentInstitution.latitude === undefined || this.currentInstitution.longitude === undefined) {
      this.toastr.warning('You Don\'t have a location set, setting default location.');
      this.currentInstitution.latitude = 36.7832;
      this.currentInstitution.longitude = 10.1843;
    }
    if (this.map) {
      this.map.remove();
    }
    this.map = L.map('map').setView([this.currentInstitution.latitude, this.currentInstitution.longitude], 15);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {}).addTo(this.map);
     L.marker([this.currentInstitution.latitude, this.currentInstitution.longitude], {
      draggable: false
    }).addTo(this.map);
  }
}
