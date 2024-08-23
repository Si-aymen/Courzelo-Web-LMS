import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { dateRangeValidator } from 'src/app/shared/models/admission/Validator';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { AdmissionService } from 'src/app/shared/services/admission/admission.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-create-admission',
  templateUrl: './create-admission.component.html',
  styleUrls: ['./create-admission.component.scss'],
  providers: [DatePipe] // Add DatePipe as a provider
})
export class CreateAdmissionComponent implements OnInit {

  formAdmission: FormGroup;
  connectedUser: UserResponse;
  instituionId:any;
  constructor(
    private formBuilder: FormBuilder,
    private sessionStorageService: SessionStorageService,
    private admissionService: AdmissionService,
    private datePipe: DatePipe ,
    private router:Router,
  ) {}

  ngOnInit(): void {
    this.createForm();
    this.connectedUser = this.sessionStorageService.getUserFromSession();
    console.log("eeeee",this.connectedUser.education)
    this.instituionId=this.connectedUser.education.institutionID;
  }

  createForm() {
    this.formAdmission = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      user: [''],
      universityId:[''],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      places: ['', Validators.required],
      waiting: ['', Validators.required]
    }, { validators: dateRangeValidator() });
  }

  get startDate() {
    return this.formAdmission.get('startDate');
  }

  get endDate() {
    return this.formAdmission.get('endDate');
  }

  // To check validation errors
  get dateRangeInvalid() {
    return this.formAdmission.hasError('dateRangeInvalid');
  }

  middle() {
    const startDateFormatted = this.datePipe.transform(this.formAdmission.get('startDate').value, 'yyyy-MM-dd HH:mm:ss');
      const endDateFormatted = this.datePipe.transform(this.formAdmission.get('endDate').value, 'yyyy-MM-dd HH:mm:ss');
  
    this.formAdmission.patchValue({
      user: this.connectedUser.email,
      startDate: startDateFormatted,
      endDate: endDateFormatted,
      universityId:this.instituionId 
    });
    console.log("form value",this.formAdmission.value)}


    onSubmit() {
      // Format the startDate and endDate
      const startDateFormatted = this.datePipe.transform(this.formAdmission.get('startDate').value, 'yyyy-MM-dd HH:mm:ss');
      const endDateFormatted = this.datePipe.transform(this.formAdmission.get('endDate').value, 'yyyy-MM-dd HH:mm:ss');
    
      // Patch formatted dates into the form
      this.formAdmission.patchValue({
        startDate: startDateFormatted,
        endDate: endDateFormatted,
        user: this.connectedUser.email,
        universityId: this.instituionId
      });
    
      // Ensure fields are integers
      const places = Number(this.formAdmission.get('places').value);
      const waiting = Number(this.formAdmission.get('waiting').value);
    
      console.log("Places:", places);
      console.log("Waiting:", waiting);
    
      // Patch the integer values back into the form
      this.formAdmission.patchValue({
        places: places,
        waiting: waiting
      });
    
      // Confirm and submit form
      Swal.fire({
        title: 'Confirm Submission',
        text: "Are you sure you want to submit this admission form?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#007bff',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, submit!',
        cancelButtonText: 'No, cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          this.admissionService.create(this.formAdmission.value).subscribe((res) => {
            console.log("Submission result:", res);
            Swal.fire({
              title: 'Submitted!',
              text: 'Your admission has been submitted successfully.',
              icon: 'success',
              confirmButtonText: 'OK'
            });
          }, (error) => {
            console.error("Error submitting form", error);
            Swal.fire({
              title: 'Error!',
              text: 'There was an issue submitting your form. Please try again.',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          });
        }
      });
      this.router.navigate(['admission/list']); 
    }
    
}
