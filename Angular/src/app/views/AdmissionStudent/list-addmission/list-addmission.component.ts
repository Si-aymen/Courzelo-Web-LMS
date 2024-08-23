import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { AdmissionService } from 'src/app/shared/services/admission/admission.service';
import { ApplicationService } from 'src/app/shared/services/admission/application.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-list-addmission',
  templateUrl: './list-addmission.component.html',
  styleUrls: ['./list-addmission.component.scss']
})
export class ListAddmissionComponent implements OnInit{
  Admissions:any[]=[];
  AllAdmissions:any[]=[];
  applicationForm: FormGroup;
  connectedUser:UserResponse;
    constructor(private admissionService:AdmissionService,
      private sessionsStorage:SessionStorageService,
      private formBuilder:FormBuilder,
      private applicationService:ApplicationService
      ){}
    ngOnInit(): void {
      this.createForm();
      this.getAll();
      this.connectedUser = this.sessionsStorage.getUserFromSession();
      this.admissionService.getAdmissions(this.connectedUser.email).subscribe((res)=>{
        console.log("le List",res)
        this.Admissions=res;
      })
    }

    getAll(){
      this.admissionService.getAllAdmissions().subscribe((res)=>{
        console.log("le List",res)
        this.AllAdmissions=res;
      })
    }
    createForm(){
      this.applicationForm = this.formBuilder.group({
        userid: [''],
        admissionid: [''],
      //universityid: [''],
      })
    }
    
    onSubmit(id: any) {
      // Patch values into the form
      this.applicationForm.patchValue({
        userid: this.connectedUser.email,
        admissionid: id
      });
  
      // SweetAlert2 confirmation dialog
      Swal.fire({
        title: 'Confirm Submission',
        text: "Are you sure you want to submit this application?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#007bff',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, submit!',
        cancelButtonText: 'No, cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          // Proceed with form submission
          this.applicationService.create(this.applicationForm.value).subscribe((res) => {
            console.log("result", res);
  
            Swal.fire({
              title: 'Submitted!',
              text: 'Your application has been submitted successfully.',
              icon: 'success',
              confirmButtonText: 'OK'
            });
          }, (error) => {
            console.error("Error submitting form", error);
  
            Swal.fire({
              title: 'Error!',
              text: 'There was an issue submitting your application. Please try again.',
              icon: 'error',
              confirmButtonText: 'OK'
            });
          });
        }
      });
    }
}
